package com.github.bruce_mig.customer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bruce_mig.customer.domain.OutboxMessage;
import com.github.bruce_mig.customer.messaging.event.CustomerDTO;
import com.github.bruce_mig.customer.messaging.event.CustomerEvent;
import com.github.bruce_mig.customer.repository.OutboxMessageRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;

@Component
@Slf4j
public class OutboxMessagePublisher {

    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;
    private final Sinks.Many<Message<?>> customerProducer;

    public OutboxMessagePublisher(OutboxMessageRepository outboxMessageRepository, ObjectMapper objectMapper, Sinks.Many<Message<?>> customerProducer) {
        this.outboxMessageRepository = outboxMessageRepository;
        this.objectMapper = objectMapper;
        this.customerProducer = customerProducer;
    }

    @Transactional
    public void deliver(){
        outboxMessageRepository.findTop10BySentOrderByIdAsc(false)
                .forEach(outboxMessage -> {
                    Message<CustomerEvent.CustomerCreated> customerCreatedMessage = mapToMessage(outboxMessage);

                    customerProducer.tryEmitNext(customerCreatedMessage);

                    outboxMessage.delivered();
                });

    }

    @SneakyThrows
    private Message<CustomerEvent.CustomerCreated> mapToMessage(final OutboxMessage outboxMessage) throws JsonProcessingException {
        String payload = outboxMessage.getPayload();
        CustomerDTO customerDTO = objectMapper.readValue(payload, CustomerDTO.class);

        Instant createdAt = outboxMessage.getCreationDate()
                .atZone(ZoneId.of("UTC"))
                .toInstant();


        var customerCreated = new CustomerEvent.CustomerCreated(
                customerDTO.id(),
                createdAt,
                customerDTO
        );

        byte[] idAsByteArray = customerCreated.customerId().toString().getBytes(StandardCharsets.UTF_8);

        return MessageBuilder.withPayload(customerCreated)
                .setHeader("X-EVENT-TYPE", outboxMessage.getEventType())
                .setHeader("X-CORRELATION-ID", customerCreated.customerId())
                .setHeader(KafkaHeaders.KEY, idAsByteArray)
                .build();
    }

}
