package com.github.bruce_mig.customer.controller.mapper;

import com.github.bruce_mig.customer.controller.dto.CustomerDTO;
import com.github.bruce_mig.customer.domain.*;

public interface CustomerMapper {

    static Customer mapToCustomer(final CustomerDTO customerDto){
        FirstName firstName = FirstName.of(customerDto.firstName());
        LastName lastName = LastName.of(customerDto.lastName());
        BirthDate birthDate = BirthDate.of(customerDto.birthDate());
        EmailAddress emailAddress = EmailAddress.of(customerDto.emailAddress());
        SSN ssn = SSN.create(customerDto.ssn());

        return Customer.create(firstName, lastName, birthDate, emailAddress, ssn);
    }
}
