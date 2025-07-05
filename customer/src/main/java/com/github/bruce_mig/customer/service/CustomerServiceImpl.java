package com.github.bruce_mig.customer.service;

import com.github.bruce_mig.customer.controller.mapper.CustomerMapper;
import com.github.bruce_mig.customer.domain.Customer;
import com.github.bruce_mig.customer.domain.EmailAddress;
import com.github.bruce_mig.customer.messaging.event.CustomerDto;
import com.github.bruce_mig.customer.messaging.event.CustomerEvent;
import com.github.bruce_mig.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.Instant;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final Sinks.Many<CustomerEvent> customerProducer;

    public CustomerServiceImpl(CustomerRepository customerRepository, Sinks.Many<CustomerEvent> customerProducer) {
        this.customerRepository = customerRepository;
        this.customerProducer = customerProducer;
    }

    @Override
    public Customer create(final Customer customer) {
        Customer customerCreated = customerRepository.save(customer);

        var customerCreatedEvent = new CustomerEvent.CustomerCreated(customerCreated.getId(), Instant.now(), CustomerMapper.mapToCustomerDto(customerCreated));
        customerProducer.tryEmitNext(customerCreatedEvent);
        return customerCreated;
    }

    @Override
    public void changeEmail(final Long customerId, final EmailAddress emailAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Couldn't find a customr by id: %s", customerId)));
        customer.changeEmail(emailAddress);
        customerRepository.save(customer);

    }

    interface CustomerMapper{
        static CustomerDto mapToCustomerDto(final Customer customerCreated){
            return new CustomerDto(customerCreated.getFirstName().getValue(),
                    customerCreated.getLastName().getValue(),
                    customerCreated.getBirthDate().getValue(),
                    customerCreated.getEmailAddress().getValue(),
                    // todo: add ssn
                    );
        }
    }
}
