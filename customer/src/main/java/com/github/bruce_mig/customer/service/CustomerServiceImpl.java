package com.github.bruce_mig.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bruce_mig.customer.domain.Customer;
import com.github.bruce_mig.customer.domain.EmailAddress;
import com.github.bruce_mig.customer.domain.OutboxMessage;
import com.github.bruce_mig.customer.messaging.event.CustomerDTO;
import com.github.bruce_mig.customer.messaging.event.CustomerEvent;
import com.github.bruce_mig.customer.repository.CustomerRepository;
import com.github.bruce_mig.customer.repository.OutboxMessageRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.Instant;

@Service
public class CustomerServiceImpl implements CustomerService{

    public static final String HEADER_NAME = "X-EVENT-TYPE";


    private final CustomerRepository customerRepository;
    private final Sinks.Many<Message<?>> customerProducer;
    private final ObjectMapper objectMapper;
    private final OutboxMessageRepository outboxMessageRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, Sinks.Many<Message<?>> customerProducer, ObjectMapper objectMapper, OutboxMessageRepository outboxMessageRepository) {
        this.customerRepository = customerRepository;
        this.customerProducer = customerProducer;
        this.objectMapper = objectMapper;
        this.outboxMessageRepository = outboxMessageRepository;
    }

    private static CustomerEvent.CustomerCreated mapToEvent(Customer customerCreated) {
        return new CustomerEvent
                .CustomerCreated(customerCreated.getId(),
                Instant.now(),
                CustomerMapper.mapToCustomerDto(customerCreated));
    }

    @SneakyThrows
    @Override
    @Transactional
    public Customer create(final Customer customer) {
        Customer customerCreated = customerRepository.save(customer);
        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDto(customerCreated);

        String payload = objectMapper.writeValueAsString(customerDTO);

        var outboxMessage = OutboxMessage.builder()
                .eventType("CustomerCreated")
                .payload(payload)
                .build();

        outboxMessageRepository.save(outboxMessage);

        return customerCreated;
    }

    @Override
    public void changeEmail(final Long customerId, final EmailAddress emailAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Couldn't find a customr by id: %s", customerId)));
        customer.changeEmail(emailAddress);
        customerRepository.save(customer);

        var customerEmailChangedEvent = new CustomerEvent.EmailChanged(customer.getId(), Instant.now(), CustomerMapper.mapToCustomerDto(customer));
        var customerEmailChangedMessage = MessageBuilder.withPayload(customerEmailChangedEvent)
                .setHeader(HEADER_NAME, "EmailChanged").build();

        customerProducer.tryEmitNext(customerEmailChangedMessage);
    }

    interface CustomerMapper{
        static CustomerDTO mapToCustomerDto(final Customer customerCreated){
            return new CustomerDTO(
                    customerCreated.getId(),
                    customerCreated.getFirstName().getValue(),
                    customerCreated.getLastName().getValue(),
                    customerCreated.getBirthDate().getValue(),
                    customerCreated.getEmailAddress().getValue(),
                    customerCreated.getSsn().getSsn()
                    );
        }
    }
}
