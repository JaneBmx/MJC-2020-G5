package com.epam.esm.model_assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.controller.TagController.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<Tag, EntityModel<Tag>> {
    @Override
    public EntityModel<Tag> toModel(Tag entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TagController.class)
                        .one(entity.getId()))
                        .withSelfRel(),
                linkTo(methodOn(TagController.class)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withRel("tags"));
    }

    @Override
    public CollectionModel<EntityModel<Tag>> toCollectionModel(Iterable<? extends Tag> entities) {
        return CollectionModel.of(((List<Tag>) entities).stream().map(this::toModel).collect(Collectors.toList()),
                linkTo(methodOn(TagController.class)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withSelfRel());
    }
}
