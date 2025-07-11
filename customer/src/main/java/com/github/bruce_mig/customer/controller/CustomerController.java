package com.github.bruce_mig.customer.controller;

import com.github.bruce_mig.customer.controller.dto.CustomerDTO;
import com.github.bruce_mig.customer.controller.dto.EmailDTO;
import com.github.bruce_mig.customer.controller.mapper.CustomerMapper;
import com.github.bruce_mig.customer.domain.*;
import com.github.bruce_mig.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody final CustomerDTO customerDto){

        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        Customer createdCustomer = customerService.create(customer);

        return ResponseEntity.ok(createdCustomer); // dont expose domain model outside app
    }

    @PatchMapping("/{customerId}/email")
    public ResponseEntity<Void> changeEmail(@PathVariable final Long customerId, @RequestBody @Valid final EmailDTO emailDTO){
        customerService.changeEmail(customerId, EmailAddress.of(emailDTO.emailAddress()));
        return ResponseEntity.accepted().build();
    }

}
