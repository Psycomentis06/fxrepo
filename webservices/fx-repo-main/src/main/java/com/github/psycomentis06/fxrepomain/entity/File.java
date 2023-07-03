package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "fx_file")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public class File {
    @Id
    private String id;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<FileVariant> variants;

}
