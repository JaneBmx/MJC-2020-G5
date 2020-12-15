package com.epam.esm.model_assembler;

import com.epam.esm.controller.RestHateoasController;
import com.epam.esm.entity.baseEntity.BaseEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.controller.AbstractRestController.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class AbstractModelAssembler<T extends BaseEntity> {

    protected Class<? extends RestHateoasController<T>> controllerClass;

    public EntityModel<T> toModel(T entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(controllerClass)
                        .one(entity.getId()))
                        .withSelfRel(),
                linkTo(methodOn(controllerClass)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withRel(entity.getClass().getSimpleName().toLowerCase() + "s"));
    }

    public CollectionModel<EntityModel<T>> toCollectionModel(Iterable<? extends T> entities) {
        return CollectionModel.of(((List<T>) entities).stream().map(this::toModel).collect(Collectors.toList()),
                linkTo(methodOn(controllerClass)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withSelfRel());
    }

    protected void setControllerClass(Class<? extends RestHateoasController<T>> controllerClass) {
        this.controllerClass = controllerClass;
    }
}
