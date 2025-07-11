package com.github.bruce_mig.customer.service;

import com.github.bruce_mig.customer.domain.Customer;
import com.github.bruce_mig.customer.domain.EmailAddress;
import com.github.bruce_mig.customer.messaging.event.CustomerDTO;
import com.github.bruce_mig.customer.messaging.event.CustomerEvent;
import com.github.bruce_mig.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.support.KafkaHeaders;
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

    public CustomerServiceImpl(CustomerRepository customerRepository, Sinks.Many<Message<?>> customerProducer) {
        this.customerRepository = customerRepository;
        this.customerProducer = customerProducer;
    }

    private static CustomerEvent.CustomerCreated mapToEvent(Customer customerCreated) {
        return new CustomerEvent
                .CustomerCreated(customerCreated.getId(),
                Instant.now(),
                CustomerMapper.mapToCustomerDto(customerCreated));
    }

    @Override
    @Transactional
    public Customer create(final Customer customer) {
        Customer customerCreated = customerRepository.save(customer);

        CustomerEvent.CustomerCreated customerCreatedEvent = mapToEvent(customerCreated);

        var customerCreatedMessage = MessageBuilder.withPayload(customerCreatedEvent)
                        .setHeader(HEADER_NAME, "CustomerCreated")
                        .setHeader(KafkaHeaders.KEY, String.valueOf(customerCreatedEvent.customerId()).getBytes())
                        .build();

        customerProducer.tryEmitNext(customerCreatedMessage);
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
            return new CustomerDTO(customerCreated.getFirstName().getValue(),
                    customerCreated.getLastName().getValue(),
                    customerCreated.getBirthDate().getValue(),
                    customerCreated.getEmailAddress().getValue(),
                    customerCreated.getSsn().getSsn()
                    );
        }
    }
}
