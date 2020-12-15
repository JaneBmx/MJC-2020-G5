package com.epam.esm.controller.impl;

import com.epam.esm.controller.AbstractRestController;
import com.epam.esm.controller.RestHateoasController;
import com.epam.esm.entity.Tag;
import com.epam.esm.model_assembler.impl.TagModelAssembler;
import com.epam.esm.services.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController extends AbstractRestController<Tag> implements RestHateoasController<Tag> {
    @Autowired
    public TagController(TagService service, TagModelAssembler assembler) {
        super(service, assembler);
        super.setControllerClass(TagController.class);
    }
}