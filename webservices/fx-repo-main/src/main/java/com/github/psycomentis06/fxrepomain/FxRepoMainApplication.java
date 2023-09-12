package com.github.psycomentis06.fxrepomain;

import com.github.psycomentis06.fxrepomain.properties.StorageProperties;
import com.github.psycomentis06.fxrepomain.service.KafkaService;
import com.github.psycomentis06.fxrepomain.service.StorageService;
import com.github.psycomentis06.fxrepomain.service.TypesenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j(topic = "Main")
@SpringBootApplication
@EnableConfigurationProperties(
        StorageProperties.class
)
public class FxRepoMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(FxRepoMainApplication.class, args);
    }

    @KafkaListener(topics = KafkaService.IMAGE_TOPIC)
    public void imageTopicListener(String in) {
        System.out.println("***************Image topic sub*****************");
        System.out.println(in);
        System.out.println("***********************************************");
    }

    @Bean
    public CommandLineRunner init(StorageService storageService, TypesenseService typesenseService) {
        return (args) -> {
            var s = storageService.init();
            switch (s) {
                case UPLOAD_DIR_NOT_CREATED -> log.warn("Upload directory not created");
                case UPLOAD_DIR_CREATED -> log.info("Upload directory created");
                default -> log.info("Upload dir: No action taken");
            }
            typesenseService.init();
        };
    }

}
