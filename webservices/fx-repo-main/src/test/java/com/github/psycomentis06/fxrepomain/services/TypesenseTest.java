package com.github.psycomentis06.fxrepomain.services;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.service.TypesenseService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class TypesenseTest {

    private TypesenseService typesenseService;

    public TypesenseTest(TypesenseService typesenseService) {
        this.typesenseService = typesenseService;
    }

    @Test
    public void testDocumentImagePost() {
        ImagePost i = new ImagePost();
        i.setId(UUID.fromString("00000000-0000-0000-0000-00000").toString());
        i.setTitle("Test");
        i.setContent("Test content");
        i.setNsfw(false);
        typesenseService.addImagePostDocument(i);
    }
}
