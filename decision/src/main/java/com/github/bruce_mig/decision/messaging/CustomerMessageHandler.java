package com.github.bruce_mig.decision.messaging;

import com.github.bruce_mig.decision.domain.Decision;
import com.github.bruce_mig.decision.messaging.event.CustomerDTO;
import com.github.bruce_mig.decision.messaging.event.CustomerEvent;
import com.github.bruce_mig.decision.service.DecisionMakerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@Slf4j
public class CustomerMessageHandler {

    private final DecisionMakerService decisionMakerService;

    public CustomerMessageHandler(DecisionMakerService decisionMakerService) {
        this.decisionMakerService = decisionMakerService;
    }

//    @Bean
//    public Consumer<CustomerEvent.CustomerCreated> handleCustomerCreated(){
//        return this::handle;
//    }

    private void handle(CustomerEvent.CustomerCreated customerCreated){
        log.info("consuming the event: {}", customerCreated);
        CustomerDTO customer = customerCreated.customer();
        decisionMakerService.decide(customer.ssn(), customer.birthDate());
    }

    @Bean
    public Function<CustomerEvent.CustomerCreated, Decision> processCustomerCreated(){
        return customerCreated -> {
            log.info("Processing (transforming) the event: {}", customerCreated);
            CustomerDTO customer = customerCreated.customer();
            Decision decision = decisionMakerService.decide(customer.ssn(), customer.birthDate());
            log.info("producing the decision: {}", decision);
            return decision;
        };
    }
}
