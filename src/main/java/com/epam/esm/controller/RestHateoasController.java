package com.epam.esm.controller;

import com.epam.esm.entity.baseEntity.BaseEntity;
import com.epam.esm.pagination.RestPagination;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface RestHateoasController<T extends BaseEntity> {
    EntityModel<T> one(int id);

    EntityModel<RestPagination<T>> all(int page, int size, String sort, String sortMode);

    EntityModel<RestPagination<T>> getBy(Map<String, String> params, int page, int size, String sort, String sortMode);

    ResponseEntity<EntityModel<T>> add(T t);

    ResponseEntity<EntityModel<T>> update(T t);

    ResponseEntity<EntityModel<T>> delete(int id);
}
