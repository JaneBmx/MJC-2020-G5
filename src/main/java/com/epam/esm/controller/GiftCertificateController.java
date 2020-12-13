package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model_assembler.GiftCertificateModelAssembler;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.pagination.RestPagination;
import com.epam.esm.services.impl.GiftCertificateService;
import com.epam.esm.util.Fields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {
    private final GiftCertificateService service;
    private final GiftCertificateModelAssembler assembler;
    public static final String DEFAULT_SIZE = "20";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SORT = "id";
    public static final String DEFAULT_SORT_MODE = "asc";

    @Autowired
    public GiftCertificateController(GiftCertificateService service, GiftCertificateModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> one(@PathVariable int id) {
        return assembler.toModel(service.getById(id));
    }

    @GetMapping
    public EntityModel<RestPagination<GiftCertificate>> all(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                                            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                                            @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                                            @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        Pagination<GiftCertificate> pagination = service.getAll(page, size, sort, sortMode);
        EntityModel<RestPagination<GiftCertificate>> model =
                EntityModel.of(new RestPagination<>
                        (
                                assembler.toCollectionModel(pagination.getContent()),
                                pagination.getSize(),
                                pagination.getCurrentPage(),
                                pagination.getOverallPages()
                        ));
        return model
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .all(page, size, sort, sortMode))
                        .withSelfRel())
                .addIf(page > 1, (() -> linkTo(methodOn(GiftCertificateController.class)
                        .all(1, size, sort, sortMode))
                        .withRel("first page")))
                .addIf(page > 1, (() -> linkTo(methodOn(GiftCertificateController.class)
                        .all(page - 1, size, sort, sortMode))
                        .withRel("previous page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(GiftCertificateController.class)
                        .all(page + 1, size, sort, sortMode))
                        .withRel("next page")))
                .addIf(page < model.getContent().getOverallPages(), (() -> linkTo(methodOn(GiftCertificateController.class)
                        .all((int) model.getContent().getOverallPages(), size, sort, sortMode))
                        .withRel("last page")));
    }

    @GetMapping("/search")
    public EntityModel<RestPagination<GiftCertificate>> getBy(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                                              @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                                              @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                                              @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        //TODO
        return null;
    }

    @PostMapping
    public ResponseEntity<EntityModel<GiftCertificate>> add(@RequestBody GiftCertificate certificate) {
        EntityModel<GiftCertificate> giftCertificate = assembler.toModel(service.create(certificate));
        return ResponseEntity
                .created(giftCertificate.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(giftCertificate);
    }

    @PutMapping
    public ResponseEntity<EntityModel<GiftCertificate>> update(@RequestBody GiftCertificate certificate) {
        EntityModel<GiftCertificate> giftCertificate = assembler.toModel(service.update(certificate));
        return ResponseEntity
                .created(giftCertificate.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(giftCertificate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<GiftCertificate>> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, String> mapParams(String name, String sort, String sortType, String description, String order, String tagName) {
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
        if (tagName != null && !tagName.isEmpty())
            params.put(Fields.TAG_NAME, tagName);

        return params;
    }
}
