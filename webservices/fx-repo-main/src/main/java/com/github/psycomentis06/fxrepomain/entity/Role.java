package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    private String name;

    @ManyToMany
    private List<Permission> permissions;

    @ManyToMany
    private List<User> users;
}
