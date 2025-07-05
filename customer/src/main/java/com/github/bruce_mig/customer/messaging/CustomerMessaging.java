package com.github.bruce_mig.customer.messaging;

import com.github.bruce_mig.customer.domain.*;
import com.github.bruce_mig.customer.messaging.event.CustomerEvent;
import com.github.bruce_mig.customer.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class CustomerMessaging {

    private final CustomerService customerService;

    public CustomerMessaging(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Bean
    public Sinks.Many<CustomerEvent> customerProducer(){
        return Sinks.many().replay().latest();
    }

    @Bean
    public Supplier<Flux<CustomerEvent>> customerSupplier(){
        return () ->  customerProducer().asFlux();
    }
}
