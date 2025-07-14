package com.github.bruce_mig.decision.config;

import com.github.tomakehurst.wiremock.common.Json;
import org.jetbrains.annotations.Nullable;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerMessageVerifier{

    @Bean
    MessageVerifierSender<Message<?>> standaloneMessageVerifier(StreamBridge streamBridge){
        return new MessageVerifierSender<Message<?>>() {
            @Override
            public void send(Message<?> message, String destination, @Nullable YamlContract contract) {
                streamBridge.send("processCustomerCreated-in-0", message);
            }

            @Override
            public <T> void send(T payload, Map<String, Object> headers, String destination, @Nullable YamlContract contract) {
                Map<String, Object> newHeaders = headers != null ? new HashMap<>(headers) : new HashMap<>();
                newHeaders.put(KafkaHeaders.TOPIC, destination);
                streamBridge.send("processCustomerCreated-in-0", MessageBuilder.createMessage(payload, new MessageHeaders(newHeaders)));

            }
        };
    }

    @Bean
    JsonMessageConverter messageConverter(){
        return new NoopMessageConverter();
    }

    class NoopMessageConverter extends JsonMessageConverter{
        NoopMessageConverter(){}

        @Override
        protected Object convertPayload(Message<?> message) {
            return message.getPayload();
        }
    }
}
