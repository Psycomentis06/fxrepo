package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "fx_user")
@Data
@NoArgsConstructor
public class User {
    public static final String PASSWORD_PATTERN = "[A-Za-z0-9]{3,30}";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    @Id
    private String id;
    @Email
    private String email;
    private String username;
    private String name;
    private String surname;
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;
    @ManyToMany
    private List<Role> roles;
    @ManyToOne
    private ImageFile picture;
}
