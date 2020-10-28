package com.epam.esm.controllers;

import static com.epam.esm.util.Fields.*;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.services.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {
    private final ServiceInterface<GiftCertificate> service;

    @Autowired
    public GiftCertificateController(ServiceInterface<GiftCertificate> service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<GiftCertificate> getCertificates(
            @RequestParam(name = NAME, required = false) String name,
            @RequestParam(name = DESCRIPTION, required = false) String description,
            @RequestParam(name = SORT, required = false) String sort,
            @RequestParam(name = SORT_TYPE, required = false) String sortType,
            @RequestParam(name = TAG_NAME, required = false) String tagName
    ) {
        if (name == null && description == null && sort == null && sortType == null && tagName == null)
            return service.getAll();
        Map<String, String> params = new HashMap<>();
        if (name != null) params.put(NAME, name);
        if (description != null) params.put(DESCRIPTION, description);
        if (sort != null) params.put(SORT, sort);
        if (sortType != null) params.put(SORT_TYPE, sortType);
        if (tagName != null) params.put(TAG_NAME, tagName);
        return service.getBy(params);
    }

    @GetMapping("/{id}")
    public GiftCertificate getById(@PathVariable int id) {
        return service.getById(id);
    }

    @PostMapping("/")
    public GiftCertificate add(@RequestBody GiftCertificate certificate) {
        service.create(certificate);
        return certificate;
    }

    @PutMapping("/")
    public GiftCertificate update(@RequestBody GiftCertificate certificate) {
        service.update(certificate);
        return certificate;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);
        return "Gift certificate with id " + id + " has been deleted";
    }
}
