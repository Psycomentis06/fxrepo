package com.github.psycomentis06.fxrepomain.typesense;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration("TypesenseConfig")
@Slf4j(topic = "TypesenseConfig")
public class Config {
    @Value("${spring.typesense.protocol}")
    private String protocol = "http";
    @Value("${spring.typesense.host}")
    private String host = "localhost";
    @Value("${spring.typesense.port}")
    private String port = "8108";
    @Value("${spring.typesense.api-key}")
    private String apiKey;


    @Bean
    TypesenseConfiguration typesenseConfiguration() {
        // TODO change Node list to a separate Node builder for multi nodes declaration
        List<TypesenseNode> nodes = new ArrayList<>();
        nodes.add(new TypesenseNode(protocol, host, port));
        return new TypesenseConfiguration(nodes, Duration.ofSeconds(2), apiKey);
    }

    @Bean
    TypesenseClient typesenseClient(TypesenseConfiguration config) {
        TypesenseClient client =  new TypesenseClient(config);
        try {
            client.health.retrieve();
        } catch (Exception e) {
            log.error("Typesense client failed to connect", e);
            System.exit(1);
        }
        return client;
    }
}
