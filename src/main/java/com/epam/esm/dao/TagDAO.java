package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDAO {
    void create(Tag tag);

    List<Tag> findAll();

    List<Tag> findBy(String criteria, String value);

    void delete(Tag tag);
}
