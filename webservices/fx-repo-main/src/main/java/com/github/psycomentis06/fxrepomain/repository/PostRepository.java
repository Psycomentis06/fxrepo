package com.github.psycomentis06.fxrepomain.repository;

import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.entity.Post;
import com.github.psycomentis06.fxrepomain.entity.PostType;
import com.github.psycomentis06.fxrepomain.model.TagListModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    long countPostsByCategory(Category category);

    @Query(
            value = "SELECT new com.github.psycomentis06.fxrepomain.model.TagListModel(t.name, COUNT(p)) FROM Post p JOIN p.tags t WHERE p.postType = ?1 AND t.name LIKE %?2% GROUP BY t.name ORDER BY t.name ASC"
    )
    Page<TagListModel> findTagsByPostType(PostType postType, String query, Pageable pageable);
}
