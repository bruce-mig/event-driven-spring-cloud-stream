package com.github.bruce_mig.decision.messaging;

import com.github.bruce_mig.decision.messaging.event.CustomerDTO;
import com.github.bruce_mig.decision.messaging.event.CustomerEvent;
import com.github.bruce_mig.decision.service.DecisionMakerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class CustomerMessageHandler {

    private final DecisionMakerService decisionMakerService;

    public CustomerMessageHandler(DecisionMakerService decisionMakerService) {
        this.decisionMakerService = decisionMakerService;
    }

    @Bean
    public Consumer<CustomerEvent.CustomerCreated> handleCustomerCreated(){
        return customerCreated -> {
            log.info("consuming the event: {}", customerCreated);
            CustomerDTO customer = customerCreated.customer();
            decisionMakerService.decide(customer.ssn(), customer.birthDate());
        };
    }
}
