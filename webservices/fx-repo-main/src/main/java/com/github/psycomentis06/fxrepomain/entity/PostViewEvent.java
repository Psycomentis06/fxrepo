package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fx_post_view_event")
@NoArgsConstructor
@Data
public class PostViewEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String userAgent;
    private String ipAddr;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne()
    private Post post;
}
