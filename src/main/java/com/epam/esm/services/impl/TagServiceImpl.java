package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.Tag;
import com.epam.esm.services.ServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements ServiceInterface<Tag> {
    private final DAOInterface<Tag> dao;

    public TagServiceImpl(TagDAOImpl dao) {
        this.dao = dao;
    }

    @Override
    public List<Tag> getAll() {
        return dao.findAll();
    }

    @Override
    public Tag getById(int id) {
        return dao.findById(id);
    }

    @Override
    public List<Tag> getBy(Map<String, String> params) {
        return dao.findBy(params);
    }

    @Override
    public void update(Tag tag) {
        dao.update(tag);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }

    @Override
    public void create(Tag tag) {
        dao.create(tag);
    }
}
