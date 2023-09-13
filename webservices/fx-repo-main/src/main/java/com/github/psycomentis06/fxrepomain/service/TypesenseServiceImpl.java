package com.github.psycomentis06.fxrepomain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.psycomentis06.fxrepomain.entity.FileVariant;
import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.entity.Tag;
import com.github.psycomentis06.fxrepomain.typesense.TypesenseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;
import org.typesense.model.FieldEmbed;
import org.typesense.model.FieldEmbedModelConfig;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic = "TypesenseServiceImpl")
public class TypesenseServiceImpl implements TypesenseService {
    private final ObjectMapper objectMapper;
    private final TypesenseClient typesenseClient;

    public TypesenseServiceImpl(ObjectMapper objectMapper, TypesenseClient typesenseClient) {
        this.objectMapper = objectMapper;
        this.typesenseClient = typesenseClient;
    }

    public void init() {
        this.createImagePostCollection();
    }

    public void createImagePostCollection() {
        try {
            typesenseClient.collections(IMAGE_POST_COLLECTION_NAME).retrieve();
            return;
        } catch (Exception e) {
            log.info("Creating collection");
        }
        List<Field> fields = new ArrayList<>();
        fields.add(new Field().name("id").type(FieldTypes.STRING));
        fields.add(new Field().name("slug").type(FieldTypes.STRING));
        fields.add(new Field().name("title").type(FieldTypes.STRING).facet(true).sort(true));
        fields.add(new Field().name("content").optional(true).type(FieldTypes.STRING));
        fields.add(new Field().name("createdAt").optional(true).type(FieldTypes.INT32));
        fields.add(new Field().name("updatedAt").optional(true).type(FieldTypes.INT32));
        fields.add(new Field().name("nsfw").optional(true).type(FieldTypes.BOOL));
        fields.add(new Field().name("tags").optional(true).type(FieldTypes.STRING_ARRAY).facet(true));
        fields.add(new Field().name("category").optional(true).type(FieldTypes.STRING).facet(true).sort(true));
        fields.add(new Field().name("thumbnail").optional(true).type(FieldTypes.STRING));
        fields.add(new Field().name("image").optional(true).type(FieldTypes.STRING));
        Field embeddingField = new Field();
        embeddingField
                .name("embedding")
                .type(FieldTypes.FLOAT_ARRAY)
                .embed(
                        new FieldEmbed()
                                .from(List.of("title", "content", "tags", "category"))
                                .modelConfig(
                                        new FieldEmbedModelConfig()
                                                .modelName("ts/all-MiniLM-L12-v2")
                                )
                );
        fields.add(embeddingField);
        CollectionSchema collectionSchema = new CollectionSchema();
        collectionSchema.name(IMAGE_POST_COLLECTION_NAME).fields(fields).defaultSortingField("title");

        try {
            typesenseClient.collections().create(collectionSchema);
            log.info("Collection 'Images' created successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addImagePostDocument(ImagePost imagePost) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("id", imagePost.getId());
        doc.put("slug", imagePost.getSlug());
        doc.put("title", imagePost.getTitle());
        doc.put("content", imagePost.getContent());
        doc.put("createdAt", Timestamp.valueOf(imagePost.getCreatedAt()));
        doc.put("updatedAt", Timestamp.valueOf(imagePost.getUpdatedAt()));
        doc.put("nsfw", imagePost.isNsfw());
        doc.put("category", imagePost.getCategory().getName());
        doc.put("thumbnail", imagePost.getThumbnail());
        doc.put("tags", imagePost.getTags().stream().map(Tag::getName).toArray(String[]::new));
        doc.put("image", imagePost.getImage().getVariants().stream().findFirst().orElse(new FileVariant()).getUrl());
        try {
            log.info(objectMapper.writeValueAsString(doc));
            typesenseClient.collections(IMAGE_POST_COLLECTION_NAME).documents().create(doc);
            log.info("Document '{}' created successfully", imagePost.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Failed to document ImagePost %s.".formatted(imagePost.getId()), e);
        }
    }
}
