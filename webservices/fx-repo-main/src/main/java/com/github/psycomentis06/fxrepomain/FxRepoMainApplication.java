package com.github.psycomentis06.fxrepomain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.psycomentis06.fxrepomain.dto.ImagePostDto;
import com.github.psycomentis06.fxrepomain.entity.KafkaEvent;
import com.github.psycomentis06.fxrepomain.model.kafka.Action;
import com.github.psycomentis06.fxrepomain.model.kafka.KafkaEventModel;
import com.github.psycomentis06.fxrepomain.model.kafka.Status;
import com.github.psycomentis06.fxrepomain.model.kafka.Target;
import com.github.psycomentis06.fxrepomain.properties.StorageProperties;
import com.github.psycomentis06.fxrepomain.repository.KafkaEventRepository;
import com.github.psycomentis06.fxrepomain.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.LocalDateTime;

@Slf4j(topic = "Main")
@SpringBootApplication
@EnableConfigurationProperties(
        StorageProperties.class
)
public class FxRepoMainApplication {

    private KafkaEventRepository kafkaEventRepository;
    private ImagePostService imagePostService;
    private StorageService storageService;

    public FxRepoMainApplication(KafkaEventRepository kafkaEventRepository, ImagePostService imagePostService, StorageService storageService) {
        this.kafkaEventRepository = kafkaEventRepository;
        this.imagePostService = imagePostService;
        this.storageService = storageService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FxRepoMainApplication.class, args);
    }

    @KafkaListener(topics = {KafkaService.IMAGE_TOPIC})
    public void imageTopicListener(ConsumerRecord<String, String> in) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        switch (in.topic()) {
            case KafkaService.IMAGE_TOPIC -> {
                try {
                    var kafkaEvent = objectMapper.readValue(in.value(), KafkaEventModel.class);
                    log.info("--Image topic-- Received event: {}", kafkaEvent.eventId());
                    var savedKafkaEvent = kafkaEventRepository.findById(kafkaEvent.eventId());
                    if (savedKafkaEvent.isPresent()) {
                        log.info("--Image topic-- Event already exists: {}", kafkaEvent.eventId());
                    } else {
                        if (kafkaEvent.targets().contains(Target.MAIN)
                                && kafkaEvent.status().equals(Status.DONE)
                                && kafkaEvent.action().equals(Action.PROCESSING)) {
//                            var imagePostType = objectMapper.getTypeFactory().constructParametricType(KafkaEventModel.class, ImagePost.class);
//                            var kafkaImagePost = objectMapper.readValue(in.value(), imagePostType);
                            var kafkaImagePost = objectMapper.convertValue(kafkaEvent.payload(), ImagePostDto.class);
                            imagePostService.preprocessingUpdatePostImage(kafkaImagePost);
                            StorageServiceStatus status = storageService.delete(kafkaImagePost.getImage().getId());
                            switch (status) {
                                case FILE_NOT_FOUND -> {
                                    log.info("File not found to be removed: {}", kafkaImagePost.getImage().getId());
                                }
                                case FILE_REMOVED -> {
                                    log.info("File removed: {}", kafkaImagePost.getImage().getId());
                                }
                                case FILE_NOT_REMOVED -> {
                                    log.info("File not removed: {}", kafkaImagePost.getImage().getId());
                                }
                            }
                            KafkaEvent k = new KafkaEvent();
                            k.setId(kafkaEvent.eventId());
                            k.setTime(LocalDateTime.parse(kafkaEvent.eventTime()));
                            k.setStatus(Status.DONE);
                            k.setMessage(in.value());
                            kafkaEventRepository.save(k);
                            log.info("Event: " + kafkaEvent.eventId() + " processed and updated ImagePost successfully");
                        } else {
                            log.info("Event: " + kafkaEvent.eventId() + " not targeted to main");
                        }
                    }
                } catch (JsonProcessingException e) {
                    log.error("Error parsing json", e);
                }
            }
            case KafkaService.VIDEO_TOPIC -> {
                log.info("Video topic");
            }
            case KafkaService.AUDIO_TOPIC -> {
                log.info("Audio topic");
            }
            case KafkaService.VECTOR_TOPIC -> {
                log.info("Vector topic");
            }
            default -> {
                log.info("Unknown topic");
            }
        }
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
