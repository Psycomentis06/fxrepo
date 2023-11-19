package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Table(name = "fx_user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Email
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String name;
    private String surname;
    private String password;
    @ManyToMany
    private List<Role> roles;
    @ManyToOne
    private ImageFile picture;
}
