package com.github.psycomentis06.fxrepomain.entity;

import com.github.psycomentis06.fxrepomain.model.kafka.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class KafkaEvent {
    @Id
    private String id;

    private LocalDateTime time;
    private String message;

    private Status status;
}
