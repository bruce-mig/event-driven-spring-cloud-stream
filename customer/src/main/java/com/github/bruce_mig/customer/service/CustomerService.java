package com.github.bruce_mig.customer.service;

import com.github.bruce_mig.customer.domain.Customer;
import com.github.bruce_mig.customer.domain.EmailAddress;

public interface CustomerService {

    Customer create(Customer customer);

    void changeEmail(Long customerId, EmailAddress emailAddress);
}
