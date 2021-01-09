package com.epam.esm.controller;

import com.epam.esm.entity.base.BaseEntity;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.pagination.RestPagination;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Criteria;
import com.epam.esm.util.Operator;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class AbstractRestController<T extends BaseEntity> {
    protected final ServiceInterface<T> service;
    protected final RepresentationModelAssembler<T, EntityModel<T>> assembler;
    protected Class<? extends RestHateoasController<T>> controllerClass;
    public static final String DEFAULT_SIZE = "20";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SORT = "id";
    public static final String DEFAULT_SORT_MODE = "asc";
    public static final String VALUE_OPERATOR_SPLITTER = ":";
    public static final Operator DEFAULT_OPERATOR = Operator.EQUAL;

    protected AbstractRestController(ServiceInterface<T> service, RepresentationModelAssembler<T, EntityModel<T>> assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public EntityModel<T> one(@PathVariable int id) {
        return assembler.toModel(service.getById(id));
    }

    @GetMapping
    public EntityModel<RestPagination<T>> all(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                              @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                              @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        Pagination<T> pagination = service.getAll(page, size, sort, sortMode);
        EntityModel<RestPagination<T>> model =
                EntityModel.of(new RestPagination<>
                        (
                                assembler.toCollectionModel(pagination.getContent()),
                                pagination.getSize(),
                                pagination.getCurrentPage(),
                                pagination.getOverallPages()
                        ));
        return model
                .add(linkTo(methodOn(controllerClass)
                        .all(page, size, sort, sortMode))
                        .withSelfRel())
                .addIf(page > 1, (() -> linkTo(methodOn(controllerClass)
                        .all(1, size, sort, sortMode))
                        .withRel("first page")))
                .addIf(page > 1, (() -> linkTo(methodOn(controllerClass)
                        .all(page - 1, size, sort, sortMode))
                        .withRel("previous page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(controllerClass)
                        .all(page + 1, size, sort, sortMode))
                        .withRel("next page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(controllerClass)
                        .all((int) model.getContent().getOverallPages(), size, sort, sortMode))
                        .withRel("last page")));
    }

    @GetMapping("/search")
    public EntityModel<RestPagination<T>> getBy(@RequestParam Map<String, String> params,
                                                @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                                @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                                @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                                @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        params.remove("page");
        params.remove("size");
        params.remove("sort");
        params.remove("sort_mode");
        List<Criteria> criteria = mapCriteria(params);
        Pagination<T> pagination = service.getBy(criteria, page, size, sort, sortMode);
        EntityModel<RestPagination<T>> model =
                EntityModel.of(new RestPagination<>
                        (
                                assembler.toCollectionModel(pagination.getContent()),
                                pagination.getSize(),
                                pagination.getCurrentPage(),
                                pagination.getOverallPages()
                        ));
        return model
                .add(linkTo(methodOn(controllerClass)
                        .getBy(params, page, size, sort, sortMode))
                        .withSelfRel())
                .addIf(page > 1, (() -> linkTo(methodOn(controllerClass)
                        .getBy(params, 1, size, sort, sortMode))
                        .withRel("first page")))
                .addIf(page > 1, (() -> linkTo(methodOn(controllerClass)
                        .getBy(params, page - 1, size, sort, sortMode))
                        .withRel("previous page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(controllerClass)
                        .getBy(params, page + 1, size, sort, sortMode))
                        .withRel("next page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(controllerClass)
                        .getBy(params, (int) model.getContent().getOverallPages(), size, sort, sortMode))
                        .withRel("last page")));
    }

    @PostMapping
    public ResponseEntity<EntityModel<T>> add(@RequestBody T t) {
        EntityModel<T> em = assembler.toModel(service.create(t));
        return ResponseEntity
                .created(em.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(em);
    }

    @PutMapping
    public ResponseEntity<EntityModel<T>> update(@RequestBody T t) {
        EntityModel<T> em = assembler.toModel(service.update(t));
        return ResponseEntity
                .created(em.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(em);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<T>> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    protected void setControllerClass(Class<? extends RestHateoasController<T>> controllerClass) {
        this.controllerClass = controllerClass;
    }

    private List<Criteria> mapCriteria(Map<String, String> params) {
        List<Criteria> criteria = new ArrayList<>();
        params.forEach((k, v) -> {
            String[] valueAndOperator = v.split(VALUE_OPERATOR_SPLITTER);
            criteria.add(new Criteria(k.toLowerCase(), valueAndOperator[0].toLowerCase(), valueAndOperator.length == 2 ?
                    Operator.getBySortForm(valueAndOperator[1]).orElseThrow(() -> new ItemNotFoundException("Given operator is not supported"))
                    : DEFAULT_OPERATOR));
        });
        return criteria;
    }
}
