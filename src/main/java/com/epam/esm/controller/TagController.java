package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.model_assembler.TagModelAssembler;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.pagination.RestPagination;
import com.epam.esm.services.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService service;
    private final TagModelAssembler assembler;
    public static final String DEFAULT_SIZE = "20";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SORT = "id";
    public static final String DEFAULT_SORT_MODE = "asc";

    @Autowired
    public TagController(TagService service, TagModelAssembler assembler) {
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/{id}")
    public EntityModel<Tag> one(@PathVariable int id) {
        return assembler.toModel(service.getById(id));
    }

    @GetMapping
    public EntityModel<RestPagination<Tag>> all(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                                @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                                @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                                @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        Pagination<Tag> pagination = service.getAll(page, size, sort, sortMode);
        EntityModel<RestPagination<Tag>> model =
                EntityModel.of(new RestPagination<>
                        (
                                assembler.toCollectionModel(pagination.getContent()),
                                pagination.getSize(),
                                pagination.getCurrentPage(),
                                pagination.getOverallPages()
                        ));
        return model
                .add(linkTo(methodOn(TagController.class)
                        .all(page, size, sort, sortMode))
                        .withSelfRel())
                .addIf(page > 1, (() -> linkTo(methodOn(TagController.class)
                        .all(1, size, sort, sortMode))
                        .withRel("first page")))
                .addIf(page > 1, (() -> linkTo(methodOn(TagController.class)
                        .all(page - 1, size, sort, sortMode))
                        .withRel("previous page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(TagController.class)
                        .all(page + 1, size, sort, sortMode))
                        .withRel("next page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(TagController.class)
                        .all((int) model.getContent().getOverallPages(), size, sort, sortMode))
                        .withRel("last page")));
    }

    @GetMapping("/search")
    public EntityModel<RestPagination<Tag>> getBy(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                                  @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                                  @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                                  @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        //TODO
        return null;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Tag tag) {
        EntityModel<Tag> em = assembler.toModel(service.create(tag));
        return ResponseEntity
                .created(em.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(em);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Tag tag) {
        EntityModel<Tag> em = assembler.toModel(service.update(tag));
        return ResponseEntity
                .created(em.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(em);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
