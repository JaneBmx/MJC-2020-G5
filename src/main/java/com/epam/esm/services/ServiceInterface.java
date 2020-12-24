package com.epam.esm.services;

import com.epam.esm.entity.baseEntity.BaseEntity;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.util.Criteria;

import java.util.List;

public interface ServiceInterface<T extends BaseEntity> {
    T getById(int id);

    Pagination<T> getAll(int page, int size, String sort, String sortMode);

    Pagination<T> getBy(List<Criteria> criteria, int page, int size, String sort, String sortMode);

    T create(T t);

    T update(T t);

    void delete(int id);
}
