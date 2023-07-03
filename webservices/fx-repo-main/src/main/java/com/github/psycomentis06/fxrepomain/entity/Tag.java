package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fx_tag")
@Data
@NoArgsConstructor
public class Tag {
    @Id
    private String name;

    @PrePersist
    @PreUpdate
    void onPersistOrUpdate() {
        this.name = this.name.toLowerCase();
    }
}
