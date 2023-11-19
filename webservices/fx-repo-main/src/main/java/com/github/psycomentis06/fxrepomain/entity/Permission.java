package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "fx_permission")
@Data
@NoArgsConstructor
public class Permission {

   public static final String READ = "r";
   public static final String WRITE = "w";
   public static final String UPDATE = "u";
   public static final String DELETE = "d";

   @Id
   private String name;

   @ManyToMany
   private List<Role> roles;
}
