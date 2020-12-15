package com.epam.esm.controller.impl;

import com.epam.esm.controller.AbstractRestController;
import com.epam.esm.controller.RestHateoasController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.model_assembler.impl.UserModelAssembler;
import com.epam.esm.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractRestController<User> implements RestHateoasController<User> {

    @Autowired
    public UserController(UserService service, UserModelAssembler assembler) {
        super(service, assembler);
        super.setControllerClass(UserController.class);
    }

    @PutMapping("/{userId}/{gcId}")
    public ResponseEntity<?> update(@PathVariable int userId, @PathVariable int gcId) {
        EntityModel<User> em = assembler.toModel(((UserService)service).addCertificate(userId, gcId));
        return ResponseEntity
                .created(em.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(em);
    }
}
