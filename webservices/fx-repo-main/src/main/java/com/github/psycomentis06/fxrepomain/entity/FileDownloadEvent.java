package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fx_file_download_event")
@Data
@NoArgsConstructor
public class FileDownloadEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String userAgent;
    private String ipAddr;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private User user;
    @ManyToOne()
    private File file;
}
