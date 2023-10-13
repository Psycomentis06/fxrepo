package com.github.psycomentis06.fxrepomain.entity;

import com.github.psycomentis06.fxrepomain.model.kafka.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "fx_kafka_event")
public class KafkaEvent {
    @Id
    private String id;

    private LocalDateTime time;
    @Column(columnDefinition = "TEXT")
    private String message;

    private Status status;
}
