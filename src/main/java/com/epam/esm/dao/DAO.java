package com.epam.esm.dao;

import com.epam.esm.pagination.Pagination;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface DAO<T extends Serializable> {
    Optional<T> getById(int id);

    Pagination<T> getAll(Pagination<T> pagination, Pair<String, String> sortParams);

    Pagination<T> getBy(Map<String, Pair<String, String>> filterParams, Pair<String, String> sortParams, Pagination<T> p);

    Optional<T> save(T t);

    void delete(T t);
}
