package com.github.psycomentis06.fxrepomain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.psycomentis06.fxrepomain.entity.FileVariant;
import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.entity.Tag;
import com.github.psycomentis06.fxrepomain.model.records.ImagePostListModel;
import com.github.psycomentis06.fxrepomain.typesense.TypesenseClient;
import com.github.psycomentis06.fxrepomain.util.Sort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.typesense.api.FieldTypes;
import org.typesense.model.*;

import java.sql.Timestamp;
import java.util.*;

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
        updateImagePostDocument(imagePost, true);
    }

    public void createImagePostDocument(Map<String, Object> doc) {
        try {
            log.info(objectMapper.writeValueAsString(doc));
            typesenseClient.collections(IMAGE_POST_COLLECTION_NAME).documents().create(doc);
            log.info("Document '{}' created successfully", doc.get("id").toString());
        } catch (JsonProcessingException jsonException) {
            throw new RuntimeException(jsonException);
        } catch (Exception e1) {
            log.error("Failed to document ImagePost %s.".formatted(doc.get("id").toString()), e1);
        }
    }

    public void updateImagePostDocument(ImagePost imagePost, boolean insertIfNotFound) {
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
        String imageUrl = imagePost
                .getImage()
                .getVariants()
                .stream()
                .min(
                        Comparator.comparing(FileVariant::getWidth)
                                .thenComparing(Comparator.comparing(FileVariant::getHeight))
                )
                .orElseGet(FileVariant::new)
                .getUrl();
        doc.put("image", imageUrl);

        if (insertIfNotFound) {
            try {
                Map<String, Object> savedDoc = typesenseClient.collections(IMAGE_POST_COLLECTION_NAME).documents(imagePost.getId()).retrieve();
                if (savedDoc != null) {
                    typesenseClient.collections(IMAGE_POST_COLLECTION_NAME).documents(imagePost.getId()).update(doc);
                    log.info("Document '{}' updated successfully", imagePost.getId());
                }
            } catch (Exception e) {
                createImagePostDocument(doc);
            }
        } else {
            try {
                typesenseClient.collections(IMAGE_POST_COLLECTION_NAME).documents(imagePost.getId()).update(doc);
            } catch (Exception exception) {
                log.error("Error updating document", exception);
            }
        }
    }

    public SearchResult getImagePosts(String query, int page, int perPage, String sortField, String direction) {
        String sortF = Sort.getSortAttributeName(ImagePostListModel.class, sortField, "title");
        if (query == null) query = "*";
        if (perPage == 0) perPage = 10;
        if (direction.length() == 0) direction = "asc";
        SearchParameters params = new SearchParameters()
                .q(query)
                .queryBy("embedding,title,content,category,tags")
//                .sortBy(sortF + ":" + direction)
                .page(page)
                .perPage(perPage);
        try {
            return typesenseClient.collections(IMAGE_POST_COLLECTION_NAME).documents().search(params);
        } catch (Exception e) {
            log.error("Error searching documents", e);
            return null;
        }
    }
}
