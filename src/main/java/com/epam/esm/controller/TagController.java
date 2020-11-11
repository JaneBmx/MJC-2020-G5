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
    public List<Tag> findTags(@RequestParam(name = Fields.NAME, required = false) String name,
                              @RequestParam(name = Fields.SORT, required = false) String sort,
                              @RequestParam(name = Fields.SORT_TYPE, required = false) String sortType,
                              @RequestParam(name = Fields.DESCRIPTION, required = false) String description,
                              @RequestParam(name = Fields.ORDER, required = false) String order) {
        return tagService.getBy(mapParams(name, sort, sortType, description, order));
    }

    private Map<String, String> mapParams(String name, String sort, String sortType, String description, String order) {
        Map<String, String> params = new HashMap<>();
        if (name != null && !name.isEmpty())
            params.put(Fields.NAME, name);
        if (sort != null && !sort.isEmpty())
            params.put(Fields.SORT, sort);
        if (sortType != null && !sortType.isEmpty())
            params.put(Fields.SORT_TYPE, sortType);
        if (description != null && !description.isEmpty())
            params.put(Fields.DESCRIPTION, description);
        if (order != null && !order.isEmpty())
            params.put(Fields.ORDER, order);

        return params;
    }
}
