package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.dto.FileVariantDto;
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
        savedImage.setAverageHash(imagePostDto.getImage().getAverageHash());
        savedImage.setColorHash(imagePostDto.getImage().getColorHash());
        savedImage.setPerceptualHash(imagePostDto.getImage().getPerceptualHash());
        savedImage.setDifferenceHash(imagePostDto.getImage().getDifferenceHash());
        var savedVariants = savedImage.getVariants();
        imagePostDto.getImage().getVariants().forEach(variant -> {
            var savedVariant = savedVariants
                    .stream()
                    .filter(savedVariant1 -> savedVariant1.getTitle().equals(variant.getTitle()))
                    .findFirst()
                    .orElse(null);
            if (savedVariant == null) {
                var variant1 = new FileVariant();
                savedVariants.add(updateFileVariant(variant1, variant));
            } else {
                savedVariants.add(updateFileVariant(savedVariant, variant));
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

    private FileVariant updateFileVariant(FileVariant fileVariant, FileVariantDto fileVariantDto) {
        fileVariant.setTitle(fileVariantDto.getTitle());
        fileVariant.setSize(fileVariantDto.getSize());
        fileVariant.setUrl(fileVariantDto.getUrl());
        fileVariant.setWidth(fileVariantDto.getWidth());
        fileVariant.setHeight(fileVariantDto.getHeight());
        fileVariant.setMd5(fileVariantDto.getMd5());
        fileVariant.setSha256(fileVariantDto.getSha256());
        fileVariant.setOriginal(fileVariantDto.isOriginal());
        return fileVariant;
    }
}
