package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Fields;
import com.epam.esm.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GiftCertificateController {
    @Autowired
    private ServiceInterface<GiftCertificate> service;

    @GetMapping("/certificates")
    public List<GiftCertificate> getGiftCertificates() {
        return service.getAll();
    }

    @GetMapping("/certificates/{id}")
    public GiftCertificate getById(@PathVariable int id) {
        GiftCertificate giftCertificate = service.getById(id);
        if (giftCertificate == null)
            throw new ItemNotFoundException("GiftCertificate with id " + id + " not found!");

        return giftCertificate;
    }

    @PostMapping("/certificates/")
    public GiftCertificate add(@RequestBody GiftCertificate certificate) {
        ValidationUtil.validate(certificate);
        return service.save(certificate);
    }

    @PutMapping("/certificates/")
    public GiftCertificate update(@RequestBody GiftCertificate certificate) {
        ValidationUtil.validate(certificate);

        return service.update(certificate);
    }

    @DeleteMapping("/certificates/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);

        return "Gift certificate with id " + id + " has been deleted";
    }

    @GetMapping("/certificates/find")
    public List<GiftCertificate> findTags(@RequestParam(name = Fields.NAME, required = false) String name,
                                          @RequestParam(name = Fields.SORT, required = false) String sort,
                                          @RequestParam(name = Fields.SORT_TYPE, required = false) String sortType,
                                          @RequestParam(name = Fields.DESCRIPTION, required = false) String description,
                                          @RequestParam(name = Fields.ORDER, required = false) String order) {
        return service.getBy(mapParams(name, sort, sortType, description, order));
    }

    private Map<String, String> mapParams(String name, String sort, String sortType, String description, String order) {
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

        return params;
    }
}
