package com.epam.esm.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DAOInterface<T> {
    Optional<T> create(T t);

    Optional<List<T>> findAll();

    Optional<List<T>> findBy(Map<String, String> params);

    int delete(int id);

    Optional<T> update(T t);

    Optional<T> findById(int id);

    Optional<T> findByName(String name);
}
