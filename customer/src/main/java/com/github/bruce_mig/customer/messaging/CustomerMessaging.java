package com.github.bruce_mig.customer.messaging;

import com.github.bruce_mig.customer.domain.*;
import com.github.bruce_mig.customer.service.CustomerService;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CustomerMessaging {

    private final CustomerService customerService;

    public CustomerMessaging(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Bean
    public Supplier<Customer> customerSupplier(){
        return () ->  {

            Customer customerCreated = Customer.create(
                    FirstName.of("John"),
                    LastName.of("Doe"),
                    BirthDate.of(LocalDate.of(1985, 11, 30)),
                    EmailAddress.of("jd@example.com")

            );
            return customerService.create(customerCreated);
        };
    }
}
