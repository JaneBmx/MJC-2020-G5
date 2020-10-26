package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.impl.CertificateDAOImpl;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Fields;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GiftCertificateServiceImpl implements ServiceInterface<GiftCertificate> {
    private final DAOInterface<GiftCertificate> certificateDao;
    private final DAOInterface<Tag> tagDao;

    public GiftCertificateServiceImpl(CertificateDAOImpl CertificateDao, TagDAOImpl tagDAO) {
        this.certificateDao = CertificateDao;
        this.tagDao = tagDAO;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return certificateDao.findAll();
    }

    @Override
    public GiftCertificate getById(int id) {
        Map<String, String> params = new HashMap<>();
        params.put(Fields.ID, String.valueOf(id));
        return certificateDao.findBy(params).get(0);
    }

    @Override
    public List<GiftCertificate> getBy(Map<String, String> params) {
        return certificateDao.findBy(params);
    }

    @Override
    public void update(GiftCertificate certificate) {
        certificateDao.update(certificate);
    }

    @Override
    public void delete(int id) {
        GiftCertificate tag = this.getById(id);
        if (tag == null) throw new TagNotFoundException("Gift certificate with id " + id + " was not found");
        certificateDao.delete(id);
    }

    @Override
    public void create(GiftCertificate certificate) {

    }
}
