package com.github.bruce_mig.customer.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAddress {

    private String value;

    private EmailAddress(String value){
        this.value = value;
    }

    public static EmailAddress of(String value){
        Objects.requireNonNull(value, "the email address cannot be null");
        Assert.isTrue(!value.isBlank(), " the email address cannot be empty");
        var regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        boolean matches = Pattern.compile(regexPattern).matcher(value).matches();
        Assert.isTrue(matches, "invalid email address");
        return new EmailAddress(value);
    }
}
