package com.github.psycomentis06.fxrepomain.specification;

import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.entity.Tag;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ImagePostSpecification {
    public static Specification<ImagePost> getByTagName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) return criteriaBuilder.conjunction();
            Join<Tag, ImagePost> imagePostTags = root.join("tags");
            return criteriaBuilder.equal(imagePostTags.get("name"), name);
        };
    }

    public static Specification<ImagePost> getByCategoryId(String categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) return criteriaBuilder.conjunction();
            Join<Category, ImagePost> imagePostCategory = root.join("category");
            return criteriaBuilder.equal(imagePostCategory.get("id"), categoryId);
        };
    }

    public static Specification<ImagePost> getByImageTitleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<ImagePost> getByImageDescriptionContains(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<ImagePost> isPublic() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Boolean>get("publik"), true);
    }

    public static Specification<ImagePost> isNsfw(boolean nsfw) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nsfw"), nsfw);
    }

    public static Specification<ImagePost> isNsfw() {
        return isNsfw(true);
    }

    public static Specification<ImagePost> isReady() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("ready"), true);
    }

}
