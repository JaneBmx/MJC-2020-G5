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

import java.util.*;

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
            return tagDao.findAll().orElse(new ArrayList<>());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Tag getById(int id) {
        try {
            return tagDao.findById(id).orElseThrow(()
                    -> new ItemNotFoundException(String.format("Tag with id %d not found.", id))
            );
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
            Tag tag = tagDao.findById(id).orElseThrow(()
                    -> new ItemNotFoundException(String.format("Tag with id %d not found.", id))
            );
            giftCertificateToTagDAO.deleteByTagId(tag.getId());
            tagDao.delete(tag.getId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Tag create(Tag tag) {
        try {
            return tagDao.create(tag).orElseThrow(()
                    -> new ServiceException("Tag wasn't created. Try again later.")
            );
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Tag save(Tag tag) {
        try {
            return (tag.getId() == 0)
                    ? create(tag)
                    : update(tag);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Tag> getBy(Map<String, String> params) {
        try {
            String name = Optional.of(params.get("name")).orElseThrow(()
                    -> new RequestParamsNotValidException("Tag not found. Empty tag name"));

            Tag tag = tagDao.findByName(params.get("name")).orElseThrow(()
                    -> new ItemNotFoundException(String.format("Tag with name %s not found!", name))
            );

            return new ArrayList<>(Collections.singletonList(tag));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
