package com.github.psycomentis06.fxrepomain.model.records;

import java.util.List;

public record ImagePostListModel(
        String id,
        String title,
        String slug,
        String image,
        String thumbnail,
        boolean nsfw,
        String category,
        List<String> tags
) {
}
