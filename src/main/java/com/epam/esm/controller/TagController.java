package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.services.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService service;
    private final String DEFAULT_SIZE = "20";
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_SORT = "id";
    private final String DEFAULT_SORT_MODE = "asc";

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Tag one(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping
    public Pagination<Tag> all(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                               @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                               @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                               @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        return service.getAll(size, page, sort, sortMode);
    }

    @GetMapping("/search")
    public Pagination<Tag> search(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
            @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        //TODO search logic
        return null;
    }

    @PostMapping
    public Tag add(@RequestBody Tag tag) {
        return service.create(tag);
    }

    @PutMapping
    public Tag update(@RequestBody Tag tag) {
        return service.update(tag);
    }

    @DeleteMapping("/{id}")
    public String Delete(@PathVariable int id) {
        service.delete(id);

        return "Deleted";
    }
}