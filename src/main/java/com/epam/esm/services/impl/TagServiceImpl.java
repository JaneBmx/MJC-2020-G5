package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Fields;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        Map<String, String> params = new HashMap<>();
        params.put(Fields.ID, String.valueOf(id));

        return dao.findBy(params).get(0);
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
        Tag tag = this.getById(id);
        if (tag == null) {
            throw new TagNotFoundException("Tag with id " + id + " was not found");
        }
        dao.delete(id);
    }

    @Override
    public void create(Tag tag) {
        dao.create(tag);
    }
}
