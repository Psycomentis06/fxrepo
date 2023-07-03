package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "fx_post")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Length(max = 255, min = 10, message = "Slug must be between 10 and 255 characters")
    private String slug;
    @Length(max = 255, min = 10, message = "Slug must be between 10 and 255 characters")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @PastOrPresent
    private LocalDateTime createdAt;
    @PastOrPresent
    private LocalDateTime updatedAt;
    @NotNull
    private String userId;
    // Visibility either public or private
    private boolean publik = true;
    // This means that the post is not ready yet = file still being processed and not uploaded to the
    // main storage server
    private boolean ready = false;
    private boolean nsfw;
    @ManyToMany
    private Set<Tag> tags;
    @ManyToOne
    private Category category;
    // Comments
    // likes
    // views
    // downloads
    // license

    @PrePersist()
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
