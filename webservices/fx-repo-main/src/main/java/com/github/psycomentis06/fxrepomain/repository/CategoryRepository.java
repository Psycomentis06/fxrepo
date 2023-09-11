package com.github.psycomentis06.fxrepomain.repository;

import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.entity.PostType;
import com.github.psycomentis06.fxrepomain.projection.CategoryListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    <T> Page<T> findByNameContainsIgnoreCase(Class<T> t, String name, Pageable pageable);

    Optional<Category> findByNameIgnoreCase(String name);

    @Query(
            value = "SELECT c FROM Category c JOIN Post p ON c.id = p.category.id AND p.postType = ?2 WHERE c.name LIKE %?1%"
    )
    Page<Category> findByNameContainsIgnoreCase(String name, PostType p, Pageable pageable);
}
