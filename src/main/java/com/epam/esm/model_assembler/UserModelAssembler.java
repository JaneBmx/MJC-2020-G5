package com.epam.esm.model_assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.controller.GiftCertificateController.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class)
                        .one(entity.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withRel("users"));
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
        return CollectionModel.of(((List<User>) entities).stream().map(this::toModel).collect(Collectors.toList()),
                linkTo(methodOn(UserController.class)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withSelfRel());
    }
}
