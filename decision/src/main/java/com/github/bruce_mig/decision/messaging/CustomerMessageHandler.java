package com.github.bruce_mig.decision.messaging;

import com.github.bruce_mig.decision.domain.Decision;
import com.github.bruce_mig.decision.exception.RetryableException;
import com.github.bruce_mig.decision.messaging.event.CustomerDTO;
import com.github.bruce_mig.decision.messaging.event.CustomerEvent;
import com.github.bruce_mig.decision.service.DecisionMakerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@Slf4j
public class CustomerMessageHandler {

    private final DecisionMakerService decisionMakerService;

    public CustomerMessageHandler(DecisionMakerService decisionMakerService) {
        this.decisionMakerService = decisionMakerService;
    }

    @Bean
    public Consumer<Message<CustomerEvent.CustomerCreated>> handleCustomerCreated(){
        return customerCreatedMessage -> {
            log.info("(customerCreated) message handler is handling message od type -------> {}", customerCreatedMessage.getHeaders()  // todo: check for null
                    .get("X-EVENT-TYPE"));
            CustomerEvent.CustomerCreated customerCreated = customerCreatedMessage.getPayload();
            CustomerDTO customer = customerCreated.customer();
            log.info("the message is {}", customerCreated);
            decisionMakerService.decide(customer.ssn(),customer.birthDate());
        };
    }

    @Bean
    public Consumer<Message<CustomerEvent.EmailChanged>> handleEmailChanged(){
        return emailChangedMessage -> {
            log.info("(emailChanged) message handler is handling message of type -------> {}", emailChangedMessage.getHeaders()  // todo: check for null
                    .get("X-EVENT-TYPE"));
            log.info("the message is {}", emailChangedMessage.getPayload());
        };

    }

/*    private void handle(CustomerEvent.CustomerCreated customerCreated){
        log.info("consuming the event: {}", customerCreated);
        CustomerDTO customer = customerCreated.customer();
        decisionMakerService.decide(customer.ssn(), customer.birthDate());
    }

    @Bean
    public Function<CustomerEvent.CustomerCreated, Decision> processCustomerCreated(){
        return customerCreated -> {
            log.info("Processing (transforming) the event: {}", customerCreated);
            CustomerDTO customer = customerCreated.customer();
            //consuming an API which gives timeout
*//*            if (customer.firstName().startsWith("N")){
                throw new IllegalStateException("the customer is invalid");
            } else if (customer.firstName().startsWith("F")) {
                throw new RetryableException("this exception is retryable");
            }*//*
            Decision decision = decisionMakerService.decide(customer.ssn(), customer.birthDate());
            log.info("producing the decision: {}", decision);
            return decision;
        };
    }*/
}
