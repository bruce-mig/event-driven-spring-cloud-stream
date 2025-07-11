/*
package com.github.bruce_mig.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableSchemaRegistryClient
public class SchemaRegistryConfig {

    @Primary
    @Bean
    public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.schemaRegistryClient.endpoint}") String endpoint){
        ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
        client.setEndpoint(endpoint);
        return client;
    }
}
*/
