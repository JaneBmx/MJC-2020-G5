package com.epam.esm.controller;

import static com.epam.esm.util.Fields.*;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.RequestParamsNotValidException;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.validation.GiftCertificateRequestParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GiftCertificateController {
    private final ServiceInterface<GiftCertificate> service;
    private final GiftCertificateRequestParamValidator validator;

    @Autowired
    public GiftCertificateController(ServiceInterface<GiftCertificate> service) {
        this.service = service;
        validator = new GiftCertificateRequestParamValidator();
    }

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
        if (!validator.isValidGiftCertificate(certificate))
            throw new RequestParamsNotValidException("Gift certificate can't be created! Params not valid!");

        return service.save(certificate);
    }

    @PutMapping("/certificates/")
    public GiftCertificate update(@RequestBody GiftCertificate certificate) {
        if(!validator.isValidGiftCertificate(certificate))
            throw new RequestParamsNotValidException("Gift certificate can't be updated! Params not valid!");

         return service.update(certificate);
    }

    @DeleteMapping("/certificates/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);

        return "Gift certificate with id " + id + " has been deleted";
    }

    @GetMapping("/certificates/find")
    public List<GiftCertificate> findCertificates(
            @RequestParam(name = NAME, required = false) String name,
            @RequestParam(name = DESCRIPTION, required = false) String description,
            @RequestParam(name = SORT, required = false) String sort,
            @RequestParam(name = SORT_TYPE, required = false) String sortType,
            @RequestParam(name = TAG_NAME, required = false) String tagName
    ) {
        Map<String, String> params = new HashMap<>();
        if (name != null)
            params.put(NAME, name);
        if (description != null)
            params.put(DESCRIPTION, description);
        if (sort != null)
            params.put(SORT, sort);
        if (sortType != null)
            params.put(SORT_TYPE, sortType);
        if (tagName != null)
            params.put(TAG_NAME, tagName);

        return service.getBy(params);
    }
}
