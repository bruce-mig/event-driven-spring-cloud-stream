package com.github.bruce_mig.customer.contracts;

import com.github.bruce_mig.customer.config.IntegrationTestBaseConfig;
import com.github.bruce_mig.customer.domain.*;
import com.github.bruce_mig.customer.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@AutoConfigureMessageVerifier
public class ShouldPublishCustomerCreatedBase extends IntegrationTestBaseConfig {

    @Autowired
    private CustomerService customerService;

    void shouldPublishCustomerCreated(){
        var customer = Customer.create(
                FirstName.of("Pierre"),
                LastName.of("Bourne"),
                BirthDate.of(LocalDate.of(1996,12,20)),
                EmailAddress.of("pb@gmail.com"),
                SSN.create(888888888)
        );

        Customer customerCreated = customerService.create(customer);
        Assertions.assertNotNull(customerCreated);
    }
}
