package com.github.psycomentis06.fxrepomain.entity;

import com.github.psycomentis06.fxrepomain.events.CategoryEntityEventHandler;
import com.github.psycomentis06.fxrepomain.validator.RgbaColorString;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@EntityListeners(CategoryEntityEventHandler.class)
@Entity
@Table(name = "fx_category")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Length(max = 50, message = "Category name is limited to 50 characters")
    @Column(unique = true)
    private String name;
    @Length(max = 1000, message = "Description is limited to 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
    private String svgIcon;
    @URL(message = "Link is not valid")
    private String thumbnail;
    @Column(length = 21)
    @RgbaColorString
    private String bgColor;
    @Column(length = 21)
    @RgbaColorString
    private String fgColor;
    @Column(length = 21)
    @RgbaColorString
    private String color;
    @OneToMany(mappedBy = "category")
    private Set<Post> posts;

    @PrePersist
    @PreUpdate
    void defaultOp() {
        this.name = name.toLowerCase();
    }
}
