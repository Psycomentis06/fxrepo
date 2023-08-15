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

    @Enumerated(EnumType.STRING)
    private FileServicePlacement placement = FileServicePlacement.MAIN_SERVICE;

    // Flag indicating to the Garbage Collector (Not the VM GC it's our little program that removes
    // unused saved files) that this file is not necessarily linked to a post but
    // should not be removed/cleaned
    private boolean orphan = false;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<FileVariant> variants;

}
