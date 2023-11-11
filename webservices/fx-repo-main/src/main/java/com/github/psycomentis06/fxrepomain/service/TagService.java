package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.Tag;
import com.github.psycomentis06.fxrepomain.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getOrCreateTag(String name) {
        name = name.trim();
        if (name.isEmpty()) name = "default";
        else if (name.length() < 3) return null;
        var t = tagRepository.findById(name.toLowerCase());
        if (t.isPresent()) {
            return t.get();
        }
        var nt = new Tag();
        nt.setName(name);
        return tagRepository.save(nt);
    }
}
