package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.GiftCertificateToTagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.RequestParamsNotValidException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.services.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements ServiceInterface<Tag> {
    private final DAOInterface<Tag> tagDao;

    private final GiftCertificateToTagDAO giftCertificateToTagDAO;

    @Autowired
    public TagServiceImpl(DAOInterface<Tag> tagDao, GiftCertificateToTagDAO giftCertificateToTagDAO) {
        this.tagDao = tagDao;
        this.giftCertificateToTagDAO = giftCertificateToTagDAO;
    }

    @Override
    @Transactional
    public List<Tag> getAll() {
        try {
            return tagDao.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Tag getById(int id) {
        try {
            return tagDao.findById(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException("Update operation not supported for Tag");
    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            Tag tag = tagDao.findById(id);
            if (tag == null) {
                throw new ItemNotFoundException("Tag with id " + id + " not found!");
            }

            giftCertificateToTagDAO.deleteByTagId(id);
            tagDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(Tag tag) {
        try {
            tagDao.create(tag);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Tag save(Tag tag) {
        try {
            tagDao.create(tag);

            return tagDao.findByName(tag.getName());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public Tag getByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new RequestParamsNotValidException("Tag not found! Empty tag name!");
        }
        return tagDao.findByName(name);

    }

    @Override
    public List<Tag> getBy(Map<String, String> params) {
        try {
            String name = params.get("name");
            if (name == null || name.isEmpty()) {
                throw new RequestParamsNotValidException("Tag not found. Empty tag name");
            }
            Tag tag = tagDao.findByName(params.get("name"));
            if (tag == null) {
                throw new ItemNotFoundException("Tag with name " + params.get("name") + " not found!");
            }
            List<Tag> tags = new ArrayList<>();
            tags.add(tag);
            return tags;
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
