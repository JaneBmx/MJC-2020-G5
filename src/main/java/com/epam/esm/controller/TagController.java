package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RequestParamsNotValidException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Fields;
import com.epam.esm.validation.TagRequestParamValidator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TagController {
    private final ServiceInterface<Tag> tagService;
    private final TagRequestParamValidator validator;

    public TagController(ServiceInterface<Tag> tagService) {
        this.tagService = tagService;
        validator = new TagRequestParamValidator();
    }

    @GetMapping("/tags")
    public List<Tag> getTags() {
        List<Tag> tags = tagService.getAll();
        if (tags == null || tags.isEmpty())
            throw new ItemNotFoundException("No tags found!");

        return tags;
    }

    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable int id) {
        Tag tag = tagService.getById(id);
        if (tag == null)
            throw new ItemNotFoundException("Tag with id " + id + " not found!");

        return tag;
    }

    @PostMapping("/tags")
    public Tag add(@RequestBody Tag tag) {
        if (!validator.isValidTag(tag))
            throw new RequestParamsNotValidException("Tag params not valid!");

        return tagService.save(tag);
    }

    @DeleteMapping("/tags/{id}")
    public String delete(@PathVariable int id) {
        tagService.delete(id);

        return "Tag with id " + id + " has been deleted";
    }

    @GetMapping("/tags/find")
    public Tag findTags(@RequestParam(name = Fields.NAME, required = false) String name) {
        return tagService.getBy(mapParams(name)).get(0);
    }

    private Map<String, String> mapParams(String name) {
        Map<String, String> params = new HashMap<>();
        if (name != null && !name.isEmpty())
            params.put(Fields.NAME, name);

        return params;
    }
}
