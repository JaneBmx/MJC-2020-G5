package com.epam.esm.model_assembler;

import com.epam.esm.entity.baseEntity.BaseEntity;
import com.epam.esm.pagination.RestPagination;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface RestHateoasController<T extends BaseEntity> {
    EntityModel<T> one(int id);

    EntityModel<RestPagination<T>> all(int page, int size, String sort, String sortMode);

    ResponseEntity<EntityModel<T>> add(T t);

    ResponseEntity<EntityModel<T>> update(T t);

    ResponseEntity<EntityModel<T>> delete(int id);
}
