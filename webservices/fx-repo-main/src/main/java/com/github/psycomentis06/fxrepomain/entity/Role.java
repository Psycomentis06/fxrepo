package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "fx_role")
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
