package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class Tag {
    @Id
    private String name;

    @PrePersist
    @PreUpdate
    void onPersistOrUpdate() {
        this.name = this.name.toLowerCase();
    }
}
