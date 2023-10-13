package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.dto.ImagePostDto;
import com.github.psycomentis06.fxrepomain.entity.FileServicePlacement;
import com.github.psycomentis06.fxrepomain.entity.FileVariant;
import com.github.psycomentis06.fxrepomain.entity.Tag;
import com.github.psycomentis06.fxrepomain.repository.ImagePostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ImagePostServiceImpl implements ImagePostService {
    private ImagePostRepository imagePostRepository;
    private TagService tagService;

    public ImagePostServiceImpl(ImagePostRepository imagePostRepository, TagService tagService) {
        this.imagePostRepository = imagePostRepository;
        this.tagService = tagService;
    }

    @Transactional
    @Override
    public void preprocessingUpdatePostImage(ImagePostDto imagePostDto) {
        // get saved object
        var savedPost = imagePostRepository.findById(imagePostDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("ImagePost with id %s not found".formatted(imagePostDto.getId())));

        // update post properties
        savedPost.setNsfw(imagePostDto.isNsfw());
        savedPost.setThumbnail(imagePostDto.getThumbnail());
        savedPost.setReady(true);
//        savedPost.setImage(imagePost.getImage());

        // update image variants
        var savedImage = savedPost.getImage();
        savedImage.setAccentColor(imagePostDto.getImage().getAccentColor());
        savedImage.setColorPalette(imagePostDto.getImage().getColorPalette());
        savedImage.setPlacement(FileServicePlacement.STORAGE_SERVICE);
        var savedVariants = savedImage.getVariants();
        imagePostDto.getImage().getVariants().forEach(variant -> {
            var savedVariant = savedVariants
                    .stream()
                    .filter(savedVariant1 -> savedVariant1.getTitle().equals(variant.getTitle()))
                    .findFirst()
                    .orElse(null);
            if (savedVariant == null) {
                var variant1 = new FileVariant();
                variant1.setTitle(variant.getTitle());
                variant1.setSize(variant.getSize());
                variant1.setUrl(variant.getUrl());
                variant1.setWidth(variant.getWidth());
                variant1.setHeight(variant.getHeight());
                variant1.setMd5(variant.getMd5());
                variant1.setSha256(variant.getSha256());
                variant1.setOriginal(variant.isOriginal());
                savedVariants.add(variant1);
            } else {
                savedVariants.add(savedVariant);
            }
        });
        // update tags
        Set<Tag> tags = new HashSet<>();
        imagePostDto.getTags().forEach(t -> {
            var tag = tagService.getOrCreateTag(t.getName());
            tags.add(tag);
        });
        savedPost.setTags(tags);
        imagePostRepository.save(savedPost);
    }
}
