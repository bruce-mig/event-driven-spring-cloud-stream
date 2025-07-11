package com.github.bruce_mig.customer.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class CustomerMessaging {

    @Bean
    public Sinks.Many<Message<?>> customerProducer(){
        return Sinks.many().replay().latest();
    }

    @Bean
    public Supplier<Flux<Message<?>>> customerSupplier(){
        return () ->  customerProducer().asFlux();
    }
}
