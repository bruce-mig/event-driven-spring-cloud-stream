package com.github.bruce_mig.customer.controller;

import com.github.bruce_mig.customer.controller.dto.CustomerDto;
import com.github.bruce_mig.customer.domain.*;
import com.github.bruce_mig.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerDto customerDto){
        FirstName firstName = FirstName.of(customerDto.firstName());
        LastName lastName = LastName.of(customerDto.lastName());
        BirthDate birthDate = BirthDate.of(customerDto.birthDate());
        EmailAddress emailAddress = EmailAddress.of(customerDto.emailAddress());

        Customer customer = Customer.create(firstName, lastName, birthDate, emailAddress);

        Customer createdCustomer = customerService.create(customer);
        return ResponseEntity.ok(createdCustomer); // dont expose domain model outside app
    }

}
