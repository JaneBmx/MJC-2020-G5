package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.services.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements ServiceInterface<Tag> {
    private final DAOInterface<Tag> tagDao;

    @Autowired
    public TagServiceImpl(TagDAOImpl tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.findAll();
    }

    @Override
    public Tag getById(int id) {
        return tagDao.findById(id);
    }

    @Override
    public List<Tag> getBy(Map<String, String> params) {
        return tagDao.findBy(params);
    }

    @Override
    public void update(Tag tag) {
        tagDao.update(tag);
    }

    @Override
    public void delete(int id) {
        Tag tag = tagDao.findById(id);
        if (tag == null)
            throw new TagNotFoundException("Tag with id " + id + " not found!");

        tagDao.delete(id);
    }

    @Override
    public void create(Tag tag) {
        tagDao.create(tag);
    }

    @Override
    public Tag save(Tag tag) {
        tagDao.create(tag);

        return tagDao.findByName(tag.getName()).get(0);
    }
}
