package com.epam.esm.services;

import com.epam.esm.entity.baseEntity.BaseEntity;
import com.epam.esm.pagination.Pagination;
import javafx.util.Pair;

import java.util.Map;

public interface ServiceInterface<T extends BaseEntity> {
    T getById(int id);

    Pagination<T> getAll(int page, int size, String sort, String sortMode);

    Pagination<T> getBy(Map<String, Pair<String, String>> filterParams,
                        Pair<String, String> sortParams, int page, int size);

    T create(T t);

    T update(T t);

    void delete(int id);
}
