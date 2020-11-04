package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Fields;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TagController {
    private final ServiceInterface<Tag> tagService;

    public TagController(ServiceInterface<Tag> tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public List<Tag> getTags(@RequestParam(name = Fields.NAME, required = false) String name,
                             @RequestParam(name = Fields.SORT, required = false) String sort,
                             @RequestParam(name = Fields.SORT_TYPE, required = false) String sortType) {
        if (name == null && sort == null) return tagService.getAll();

        Map<String, String> params = new HashMap<>();
        if (sort != null)
            params.put(Fields.SORT, sort);
        if (name != null)
            params.put(Fields.NAME, name);
        if (sortType != null)
            params.put(Fields.SORT_TYPE, sortType);

        return tagService.getBy(params);
    }

    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable int id) {
        Tag tag = tagService.getById(id);
        if (tag == null)
            throw new TagNotFoundException("Tag with id " + id + " not found!");

        return tag;
    }

    @PostMapping("/tags")
    public Tag add(@RequestBody Tag tag) {
        return tagService.save(tag);
    }

    @DeleteMapping("/tags/{id}")
    public String deleteTag(@PathVariable int id) {
        tagService.delete(id);

        return "Tag with id " + id + " has been deleted";
    }
}
