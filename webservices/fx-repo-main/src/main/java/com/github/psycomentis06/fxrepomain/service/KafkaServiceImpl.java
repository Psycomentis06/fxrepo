package com.github.psycomentis06.fxrepomain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.model.kafka.Action;
import com.github.psycomentis06.fxrepomain.model.kafka.KafkaEventModel;
import com.github.psycomentis06.fxrepomain.model.kafka.Status;
import com.github.psycomentis06.fxrepomain.model.kafka.Target;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

@Service
public class KafkaServiceImpl implements KafkaService {

    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public KafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishNewImagePostEvent(ImagePost p) {
        HashSet<Target> targets = new HashSet<>();
        KafkaEventModel<ImagePost> kf = new KafkaEventModel<>(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                Action.ADD,
                Status.DONE,
                Collections.singleton(Target.PRE_PROCESSING),
                p
        );
        try {
            kafkaTemplate.send(IMAGE_TOPIC, objectMapper.writeValueAsString(kf));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void consumePreprocessingImageFileEvent() {

    }
}
