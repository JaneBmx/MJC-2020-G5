package com.epam.esm.services.impl;

import com.epam.esm.dao.GenericDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.RequestParamsNotValidException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.services.ServiceInterface;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class TagService implements ServiceInterface<Tag> {
    private final GenericDAO<Tag> tagDAO;

    @Autowired
    public TagService(GenericDAO<Tag> tagDAO) {
        this.tagDAO = tagDAO;
        this.tagDAO.setClazz(Tag.class);
    }

    @Override
    @Transactional
    public Tag getById(int id) {
        return tagDAO.getById(id).orElseThrow(()
                -> new ItemNotFoundException(String.format("Tag id %d not found", id))
        );
    }

    @Override
    @Transactional
    public Pagination<Tag> getAll(int size, int page, String sort, String sortMode) {
        return tagDAO.getAll(new Pagination<>(size, page, 0), new Pair<>(sort, sortMode));
    }

    @Override
    @Transactional
    public Pagination<Tag> getBy(Map<String, Pair<String, String>> filterParams,
                                 Pair<String, String> sortParams, int page, int size) {
        //TODO search logic

        return tagDAO.getBy(filterParams, sortParams, new Pagination<>(page, size, 0));
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        if (tag == null) {
            throw new RequestParamsNotValidException("Empty body");
        }
        if (tag.getName() == null || tag.getName().trim().isEmpty()) {
            throw new ServiceException("Name required");
        }

        return tagDAO.save(tag).orElse(null);
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException("Update operation is not allowed for tag");
    }

    @Override
    @Transactional
    public void delete(int id) {
        Tag tag = tagDAO.getById(id).orElseThrow(()
                -> new ItemNotFoundException(String.format("Tag id %d not found", id))
        );

        tagDAO.delete(tag);
    }
}
