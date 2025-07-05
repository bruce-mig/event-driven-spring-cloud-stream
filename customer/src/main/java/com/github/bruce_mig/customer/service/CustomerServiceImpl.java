package com.github.bruce_mig.customer.service;

import com.github.bruce_mig.customer.domain.Customer;
import com.github.bruce_mig.customer.domain.EmailAddress;
import com.github.bruce_mig.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final Sinks.Many<Customer> customerProducer;

    public CustomerServiceImpl(CustomerRepository customerRepository, Sinks.Many<Customer> customerProducer) {
        this.customerRepository = customerRepository;
        this.customerProducer = customerProducer;
    }

    @Override
    public Customer create(final Customer customer) {
        Customer customerCreated = customerRepository.save(customer);
        customerProducer.tryEmitNext(customerCreated);
        return customerCreated;
    }

    @Override
    public void changeEmail(final Long customerId, final EmailAddress emailAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Couldn't find a customr by id: %s", customerId)));
        customer.changeEmail(emailAddress);
        customerRepository.save(customer);

    }
}
