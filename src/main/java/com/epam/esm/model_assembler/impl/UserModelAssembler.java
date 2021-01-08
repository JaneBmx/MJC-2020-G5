package com.epam.esm.model_assembler.impl;

import com.epam.esm.controller.impl.UserController;
import com.epam.esm.entity.User;
import com.epam.esm.model_assembler.AbstractModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler extends AbstractModelAssembler<User> implements RepresentationModelAssembler<User, EntityModel<User>> {
    public UserModelAssembler() {
        super.setControllerClass(UserController.class);
    }
}
