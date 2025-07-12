package com.github.bruce_mig.decision.exception;

import lombok.Value;

@Value
public class RetryableException extends RuntimeException{
    String reason;
}
