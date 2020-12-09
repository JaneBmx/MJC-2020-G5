package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.services.impl.GiftCertificateService;
import com.epam.esm.util.Fields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {
    private final GiftCertificateService service;
    private final String DEFAULT_SIZE = "20";
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_SORT = "id";
    private final String DEFAULT_SORT_MODE = "asc";

    @Autowired
    public GiftCertificateController(GiftCertificateService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public GiftCertificate one(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping
    public Pagination<GiftCertificate> all(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                           @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                           @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                           @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        return service.getAll(page, size, sort, sortMode);
    }

    @GetMapping("/search")
    public Pagination<GiftCertificate> getBy(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                             @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                             @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                             @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        //TODO
        return null;
    }

    @PostMapping
    public GiftCertificate add(@RequestBody GiftCertificate certificate) {
        return service.create(certificate);
    }

    @PutMapping
    public GiftCertificate update(@RequestBody GiftCertificate certificate) {
        return service.update(certificate);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);

        return "Deleted";
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
