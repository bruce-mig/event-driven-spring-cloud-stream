package com.github.bruce_mig.customer.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class EmailAddressTest {
    @Test
    @DisplayName("GIVEN a valid email WHEN create THEN email address is created")
    void of(){
        var emailAddress = "bmigeri@gmail.com";

        EmailAddress actualEmailAddress = EmailAddress.of(emailAddress);
        Assertions.assertNotNull(actualEmailAddress);
        Assertions.assertEquals(emailAddress, actualEmailAddress.getValue());
    }

    @Test
    @DisplayName("GIVEN an invalid email address with null value WHEN create THEN NullPointerException")
    void ofNullValue(){
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class, () -> EmailAddress.of(null));
        Assertions.assertEquals("the email address cannot be null",nullPointerException.getMessage());
    }

    @Test
    @DisplayName("GIVEN an invalid email address with bad format value WHEN create THEN IllegalArgumentException")
    void ofInvalidValue(){
        var emailAddress = "bmigeri.gmail.com";

        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> EmailAddress.of(emailAddress));
        Assertions.assertEquals("invalid email address", illegalArgumentException.getMessage());
    }
}