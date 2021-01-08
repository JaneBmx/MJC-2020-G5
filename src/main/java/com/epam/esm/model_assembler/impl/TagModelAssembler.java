package com.epam.esm.model_assembler.impl;

import com.epam.esm.controller.impl.TagController;
import com.epam.esm.entity.Tag;
import com.epam.esm.model_assembler.AbstractModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TagModelAssembler extends AbstractModelAssembler<Tag> implements RepresentationModelAssembler<Tag, EntityModel<Tag>> {
    public TagModelAssembler() {
        super.setControllerClass(TagController.class);
    }
}
