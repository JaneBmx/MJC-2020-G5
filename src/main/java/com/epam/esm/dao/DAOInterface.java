package com.epam.esm.dao;

import java.util.List;
import java.util.Map;

public interface DAOInterface<T> {

    void create(T t);

    List<T> findAll();

    List<T> findBy(Map<String, String> params);

    void delete(int id);

    void update(T t);

    T findById(int id);

    List<T> findByName(String name);
}
