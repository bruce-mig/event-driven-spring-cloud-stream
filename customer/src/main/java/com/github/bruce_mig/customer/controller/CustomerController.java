package com.github.bruce_mig.customer.controller;

import com.github.bruce_mig.customer.controller.dto.CustomerDto;
import com.github.bruce_mig.customer.controller.mapper.CustomerMapper;
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

        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        Customer createdCustomer = customerService.create(customer);

        return ResponseEntity.ok(createdCustomer); // dont expose domain model outside app
    }

}
