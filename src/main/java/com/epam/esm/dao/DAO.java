package com.epam.esm.dao;

import com.epam.esm.entity.baseEntity.BaseEntity;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.util.Criteria;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

public interface DAO<T extends BaseEntity> {
    Optional<T> getById(int id);

    Pagination<T> getAll(Pagination<T> pagination, Pair<String, String> sortParams);

    Pagination<T> getBy(List<Criteria> criteria, int page, int size, String sort, String sortMode);

    Optional<T> save(T t);

    void delete(T t);
}
