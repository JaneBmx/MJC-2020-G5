package com.epam.esm.model_assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.controller.GiftCertificateController.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class GiftCertificateModelAssembler implements RepresentationModelAssembler<GiftCertificate, EntityModel<GiftCertificate>> {
    @Override
    public EntityModel<GiftCertificate> toModel(GiftCertificate entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(GiftCertificateController.class)
                        .one(entity.getId()))
                        .withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withRel("certificates"));
    }

    @Override
    public CollectionModel<EntityModel<GiftCertificate>> toCollectionModel(Iterable<? extends GiftCertificate> entities) {
        return CollectionModel.of(((List<GiftCertificate>) entities).stream().map(this::toModel).collect(Collectors.toList()),
                linkTo(methodOn(GiftCertificateController.class)
                        .all(Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_SIZE), DEFAULT_SORT, DEFAULT_SORT_MODE))
                        .withSelfRel());
    }
}
