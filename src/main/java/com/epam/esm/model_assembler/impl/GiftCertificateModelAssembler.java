package com.epam.esm.model_assembler.impl;

import com.epam.esm.controller.impl.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model_assembler.AbstractModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateModelAssembler extends AbstractModelAssembler<GiftCertificate>
        implements RepresentationModelAssembler<GiftCertificate, EntityModel<GiftCertificate>> {

    public GiftCertificateModelAssembler() {
        super.setControllerClass(GiftCertificateController.class);
    }
}
