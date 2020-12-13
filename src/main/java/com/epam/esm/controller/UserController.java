package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.model_assembler.UserModelAssembler;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.pagination.RestPagination;
import com.epam.esm.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    private final UserModelAssembler assembler;
    public static final String DEFAULT_SIZE = "20";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SORT = "id";
    public static final String DEFAULT_SORT_MODE = "asc";

    @Autowired
    public UserController(UserService service, UserModelAssembler assembler) {
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/{id}")
    public EntityModel<User> one(@PathVariable int id) {

        return assembler.toModel(service.getById(id));
    }

    @GetMapping
    public EntityModel<RestPagination<User>> all(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                                 @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                                 @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        Pagination<User> pagination = service.getAll(page, size, sort, sortMode);
        EntityModel<RestPagination<User>> model =
                EntityModel.of(new RestPagination<>
                        (
                                assembler.toCollectionModel(pagination.getContent()),
                                pagination.getSize(),
                                pagination.getCurrentPage(),
                                pagination.getOverallPages()
                        ));
        return model
                .add(linkTo(methodOn(UserController.class)
                        .all(page, size, sort, sortMode))
                        .withSelfRel())
                .addIf(page > 1, (() -> linkTo(methodOn(UserController.class)
                        .all(1, size, sort, sortMode))
                        .withRel("first page")))
                .addIf(page > 1, (() -> linkTo(methodOn(UserController.class)
                        .all(page - 1, size, sort, sortMode))
                        .withRel("previous page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(UserController.class)
                        .all(page + 1, size, sort, sortMode))
                        .withRel("next page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(UserController.class)
                        .all((int) model.getContent().getOverallPages(), size, sort, sortMode))
                        .withRel("last page")));
    }

    @GetMapping("/search")
    public Pagination getBy(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                            @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                            @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        //TODO
        return null;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody User user) {
        EntityModel<User> em = assembler.toModel(service.create(user));
        return ResponseEntity
                .created(em.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(em);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody GiftCertificate certificate, @PathVariable int id) {
        EntityModel<User> em = assembler.toModel(service.addCertificate(certificate, id));
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
