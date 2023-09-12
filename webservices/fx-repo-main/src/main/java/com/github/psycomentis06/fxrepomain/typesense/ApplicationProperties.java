package com.github.psycomentis06.fxrepomain.typesense;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.typesense")
@Data
public class ApplicationProperties {
    private String protocol = "http";
    private String host = "localhost";
    private String port = "8108";
    private String apiKey;

}
