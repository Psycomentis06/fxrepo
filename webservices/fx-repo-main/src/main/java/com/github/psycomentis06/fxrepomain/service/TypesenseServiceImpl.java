package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.typesense.TypesenseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "TypesenseServiceImpl")
public class TypesenseServiceImpl implements TypesenseService {
    private final TypesenseClient typesenseClient;

    public TypesenseServiceImpl(TypesenseClient typesenseClient) {
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
        fields.add(new Field().name("content").type(FieldTypes.STRING));
        fields.add(new Field().name("createdAt").type(FieldTypes.STRING));
        fields.add(new Field().name("updatedAt").type(FieldTypes.STRING));
        fields.add(new Field().name("nsfw").type(FieldTypes.BOOL));
        fields.add(new Field().name("tags").type(FieldTypes.STRING_ARRAY).facet(true));
        fields.add(new Field().name("category").type(FieldTypes.STRING).facet(true).sort(true));
        fields.add(new Field().name("thumbnail").type(FieldTypes.STRING));
        fields.add(new Field().name("image").type(FieldTypes.STRING));
        CollectionSchema collectionSchema = new CollectionSchema();
        collectionSchema.name(IMAGE_POST_COLLECTION_NAME).fields(fields).defaultSortingField("title");

        try {
            typesenseClient.collections().create(collectionSchema);
            log.info("Collection 'Images' created successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
