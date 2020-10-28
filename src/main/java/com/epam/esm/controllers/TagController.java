package com.epam.esm.controllers;

import com.epam.esm.entity.Tag;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Fields;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final ServiceInterface<Tag> serviceInterface;

    public TagController(ServiceInterface<Tag> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @GetMapping("/")
    public List<Tag> getTags(@RequestParam(name = Fields.NAME, required = false) String name,
                             @RequestParam(name = Fields.SORT, required = false) String sort,
                             @RequestParam(name = Fields.SORT_TYPE, required = false) String sortType) {
        if (name == null && sort == null) return serviceInterface.getAll();
        Map<String, String> params = new HashMap<>();
        if (sort != null) {
            params.put(Fields.SORT, sort);
        }
        if (name != null) {
            params.put(Fields.NAME, name);
        }
        if (sortType != null) {
            params.put(Fields.SORT_TYPE, sortType);
        }
        return serviceInterface.getBy(params);
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable int id) {
        return serviceInterface.getById(id);
    }

    @PostMapping("/")
    public Tag addTag(@RequestBody Tag tag) {
        serviceInterface.create(tag);
        return tag;
    }

    @PutMapping("/")
    public Tag updateTag(@RequestBody Tag tag) {
        serviceInterface.update(tag);
        return tag;
    }

    @DeleteMapping("/{id}")
    public String deleteTag(@PathVariable int id) {
        serviceInterface.delete(id);
        return "Tag with id " + id + " has been deleted";
    }
}