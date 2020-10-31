package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.impl.CertificateDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.util.Fields.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GiftCertificateServiceImpl implements ServiceInterface<GiftCertificate> {
    private final DAOInterface<GiftCertificate> certificateDao;
    private final TagDAO tagDao;

    public GiftCertificateServiceImpl(CertificateDAOImpl CertificateDao, TagDAO tagDao) {
        this.certificateDao = CertificateDao;
        this.tagDao = tagDao;
    }

    @Transactional
    @Override
    public List<GiftCertificate> getAll() {
        List<GiftCertificate> certificates = certificateDao.findAll();
        certificates.forEach(c -> {
            List<Tag> tags = tagDao.getByCertificateId(c.getId());
            c.getTags().addAll(tags);
        });
        return certificates;
    }

    @Transactional
    @Override
    public GiftCertificate getById(int id) {
        Map<String, String> params = new HashMap<>();
        params.put(Fields.ID, String.valueOf(id));
        List<GiftCertificate> certificates = certificateDao.findBy(params);
        GiftCertificate certificate = certificates == null || certificates.size() == 0 ? null : certificateDao.findBy(params).get(0);
        if (certificate == null) {
            throw new GiftCertificateNotFoundException(String.format("Certificate (id=%d) not found", id));
        }
        List<Tag> tags = tagDao.getByCertificateId(id);
        certificate.getTags().addAll(tags);

        return certificate;
    }

    @Transactional
    @Override
    public List<GiftCertificate> getBy(Map<String, String> params) {
        List<GiftCertificate> certificates = certificateDao.findBy(params);
        if (certificates == null || certificates.size() == 0) {
            throw new GiftCertificateNotFoundException("Certificate not found");
        }
        certificates.forEach(c -> {
            List<Tag> tags = tagDao.getByCertificateId(c.getId());
            c.getTags().addAll(tags);
        });

        return certificates;
    }

    @Transactional
    @Override
    public void update(GiftCertificate certificate) {
        certificateDao.update(certificate);
    }

    @Transactional
    @Override
    public void delete(int id) {
        certificateDao.delete(id);
    }

    @Transactional
    @Override
    public void create(GiftCertificate certificate) {
        if (certificate.getTags() != null)
            certificate.getTags().forEach(t -> {
                Map<String, String> params = new HashMap<>();
                params.put(NAME, t.getName());
                List<Tag> tags = tagDao.findBy(params);
                if (tags == null || tags.size() == 0) tagDao.create(t);
            });

        certificateDao.create(certificate);
    }
}
