package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.GiftCertificateToTagDAO;
import com.epam.esm.dao.impl.GiftCertificateDAOImpl;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.services.ServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GiftCertificateServiceImpl implements ServiceInterface<GiftCertificate> {
    private final DAOInterface<GiftCertificate> giftCertificateDAO;
    private final TagDAOImpl tagDAO;
    private final GiftCertificateToTagDAO giftCertificateToTagDAO;

    public GiftCertificateServiceImpl(GiftCertificateDAOImpl giftCertificateDAO,
                                      TagDAOImpl tagDAO,
                                      GiftCertificateToTagDAO giftCertificateToTagDAO) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDAO = tagDAO;
        this.giftCertificateToTagDAO = giftCertificateToTagDAO;
    }

    @Transactional
    @Override
    public List<GiftCertificate> getAll() {
        List<GiftCertificate> certificates = giftCertificateDAO.findAll();

        certificates.forEach(c -> {
            List<Tag> tags = tagDAO.findByCertificateId(c.getId());
            c.getTags().addAll(tags);
        });

        return certificates;
    }

    @Transactional
    @Override
    public GiftCertificate getById(int id) {
        try {
            GiftCertificate giftCertificate = giftCertificateDAO.findById(id);
            if (giftCertificate == null)
                throw new ItemNotFoundException("Gift certificate with id " + id + " not found!");

            List<Tag> tags = tagDAO.findByCertificateId(id);
            if (tags != null && !tags.isEmpty())
                giftCertificate.getTags().addAll(tags);

            return giftCertificate;
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

    }

    @Transactional
    @Override
    public List<GiftCertificate> getBy(Map<String, String> params) {
        List<GiftCertificate> certificates = giftCertificateDAO.findBy(params);
        certificates.forEach(c -> {
            List<Tag> tags = tagDAO.findByCertificateId(c.getId());
            c.getTags().addAll(tags);
        });

        return certificates;
    }


    //@TODO add try&catch
    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public GiftCertificate update(GiftCertificate certificate) {
        GiftCertificate giftCertificate = null;
        if (certificate.getName() != null) {
            giftCertificate = giftCertificateDAO.findByName(certificate.getName());
            if (giftCertificate == null)
                giftCertificate = giftCertificateDAO.findById(certificate.getId());
            if (giftCertificate == null)
                throw new ItemNotFoundException("Update failed! Gift certificate not found!");
        }

        giftCertificate.setName(certificate.getName() == null
                ? giftCertificate.getName()
                : certificate.getName());
        giftCertificate.setDescription(certificate.getDescription() == null
                ? giftCertificate.getName()
                : certificate.getDescription());
        giftCertificate.setPrice(certificate.getPrice() == 0
                ? giftCertificate.getPrice()
                : certificate.getPrice());
        giftCertificate.setDuration(certificate.getDuration() == 0
                ? giftCertificate.getDuration()
                : certificate.getDuration());
        giftCertificateDAO.update(certificate);

        giftCertificateToTagDAO.deleteByGiftCertificateId(certificate.getId());
        if (certificate.getTags() != null && !certificate.getTags().isEmpty())
            mapTagsToGiftCertificates(certificate.getTags(), giftCertificate.getId());

        return getById(giftCertificate.getId());
    }

    /**
     * Check tags existing, create if tag not found in DB, map tags to gift certificate
     */
    private void mapTagsToGiftCertificates(Set<Tag> tags, int giftCertificateId) {
        Set<Tag> createdTags = new HashSet<>();

        for (Tag t : tags) {
            Tag tag = tagDAO.findByName(t.getName());
            if (tag == null) {
                tagDAO.create(t);
                tag = tagDAO.findByName(t.getName());
            }
            createdTags.add(tag);
        }
        for (Tag t : createdTags)
            giftCertificateToTagDAO.mapGiftCertificateToTag(giftCertificateId, t.getId());
    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            GiftCertificate giftCertificate = giftCertificateDAO.findById(id);
            if (giftCertificate == null)
                throw new ItemNotFoundException("Not deleted! GiftCertificate with id " + id + " not found!");

            giftCertificateDAO.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void create(GiftCertificate giftCertificate) {
        try {
            giftCertificateDAO.create(giftCertificate);
            GiftCertificate createdGiftCertificate = giftCertificateDAO.findByName(giftCertificate.getName());

            if (giftCertificate.getTags() != null && !giftCertificate.getTags().isEmpty())
                mapTagsToGiftCertificates(giftCertificate.getTags(), createdGiftCertificate.getId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public GiftCertificate save(GiftCertificate giftCertificate) {
        if (giftCertificate.getId() == 0) {
            create(giftCertificate);

            GiftCertificate createdGiftCertificate = giftCertificateDAO.findByName(giftCertificate.getName());
            List<Tag> tags = tagDAO.findByCertificateId(createdGiftCertificate.getId());

            if (tags != null && !tags.isEmpty())
                createdGiftCertificate.setTags(new HashSet<>(tags));

            return createdGiftCertificate;
        }
        update(giftCertificate);

        return getById(giftCertificate.getId());
    }
}
