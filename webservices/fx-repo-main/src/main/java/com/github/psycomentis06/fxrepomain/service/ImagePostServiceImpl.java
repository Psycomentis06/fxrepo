package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.entity.Tag;
import com.github.psycomentis06.fxrepomain.repository.ImagePostRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public void preprocessingUpdatePostImage(ImagePost imagePost) {
        // get saved object
        var savedPost = imagePostRepository.findById(imagePost.getId())
                .orElseThrow(() -> new EntityNotFoundException("ImagePost with id %s not found".formatted(imagePost.getId())));

        // update post properties
        savedPost.setNsfw(imagePost.isNsfw());
        savedPost.setThumbnail(imagePost.getThumbnail());
        savedPost.setReady(true);
        savedPost.setImage(imagePost.getImage());
        /*
        // update image variants
        var savedImage = savedPost.getImage();
        savedImage.setAccentColor(imagePost.getImage().getAccentColor());
        savedImage.setColorPalette(imagePost.getImage().getColorPalette());
        savedImage.setPlacement(FileServicePlacement.STORAGE_SERVICE);
        var savedVariants = savedImage.getVariants();
        imagePost.getImage().getVariants().forEach(variant -> {
            var savedVariant = savedVariants.stream().filter(savedVariant1 -> savedVariant1.getTitle().equals(variant.getTitle())).findFirst().orElse(null);
            if (savedVariant == null) {
                savedVariants.add(variant);
            } else {
                savedVariant = variant;
                savedVariants.add(savedVariant);
            }
        });
        // update tags
        */

        Set<Tag> tags = new HashSet<>();
        imagePost.getTags().forEach(t -> {
            var tag = tagService.getOrCreateTag(t.getName());
            tags.add(tag);
        });
        savedPost.setTags(tags);
        imagePostRepository.save(savedPost);
    }
}
