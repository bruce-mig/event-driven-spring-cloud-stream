package com.github.bruce_mig.decision.messaging.event;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CustomerDTO(
        @NotBlank(message = "first name cannot be blank")
        String firstName,
        @NotBlank(message = "last name cannot be blank")
        String lastName,
        @NotNull(message = "the birth date cannot be null")
        @Past(message = "the birth date is invalid, it should be in the past")
        LocalDate birthDate,
        @Email(message = "please provide a valid email address")
        @NotBlank(message = "the email address cannot be blank")
        String emailAddress,
        @NotNull(message = "Social Security Number should not be null")
        Integer ssn
) {
}
