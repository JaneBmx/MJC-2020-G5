package com.epam.esm.controller.impl;

import com.epam.esm.controller.AbstractRestController;
import com.epam.esm.controller.RestHateoasController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model_assembler.impl.GiftCertificateModelAssembler;
import com.epam.esm.services.impl.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController extends AbstractRestController<GiftCertificate> implements RestHateoasController<GiftCertificate> {
    @Autowired
    public GiftCertificateController(GiftCertificateService service, GiftCertificateModelAssembler assembler) {
        super(service, assembler);
        super.setControllerClass(GiftCertificateController.class);
    }
}
