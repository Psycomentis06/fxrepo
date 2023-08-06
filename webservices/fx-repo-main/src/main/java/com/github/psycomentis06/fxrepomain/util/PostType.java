package com.github.psycomentis06.fxrepomain.util;

public class PostType {
    public static com.github.psycomentis06.fxrepomain.entity.PostType getPostType(String type) throws IllegalArgumentException {
        com.github.psycomentis06.fxrepomain.entity.PostType postType;
        switch (type.toUpperCase()) {
            case "IMAGE", "IM" -> postType = com.github.psycomentis06.fxrepomain.entity.PostType.IMAGE;
            default -> throw new IllegalArgumentException("Invalid Post type value '%s'".formatted(type));
        }
        return postType;
    }
}
