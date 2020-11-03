package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.impl.CertificateDAOImpl;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.services.ServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.epam.esm.util.Fields.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GiftCertificateServiceImpl implements ServiceInterface<GiftCertificate> {
    private final DAOInterface<GiftCertificate> certificateDAO;
    private final TagDAOImpl tagDAO;

    public GiftCertificateServiceImpl(CertificateDAOImpl certificateDAO, TagDAOImpl tagDAO) {
        this.certificateDAO = certificateDAO;
        this.tagDAO = tagDAO;
    }

    @Transactional
    @Override
    public List<GiftCertificate> getAll() {
        List<GiftCertificate> certificates = certificateDAO.findAll();

        certificates.forEach(c -> {
            List<Tag> tags = tagDAO.findByCertificateId(c.getId());
            c.getTags().addAll(tags);
        });

        return certificates;
    }

    @Transactional
    @Override
    public GiftCertificate getById(int id) {
        GiftCertificate giftCertificate = certificateDAO.findById(id);

        List<Tag> tags = tagDAO.findByCertificateId(id);
        giftCertificate.getTags().addAll(tags);

        return giftCertificate;
    }

    @Transactional
    @Override
    public List<GiftCertificate> getBy(Map<String, String> params) {
        List<GiftCertificate> certificates = certificateDAO.findBy(params);
        certificates.forEach(c -> {
            List<Tag> tags = tagDAO.findByCertificateId(c.getId());
            c.getTags().addAll(tags);
        });

        return certificates;
    }

    @Transactional
    @Override
    public void update(GiftCertificate certificate) {
        certificateDAO.update(certificate);
    }

    @Transactional
    @Override
    public void delete(int id) {
        certificateDAO.delete(id);
    }

    @Transactional
    @Override
    public void create(GiftCertificate certificate) {
        //TODO add to tag2certificate table
        if (certificate.getTags() != null)
            certificate.getTags().forEach(t -> {
                Map<String, String> params = new HashMap<>();
                params.put(NAME, t.getName());
                List<Tag> tags = tagDAO.findBy(params);
                if (tags == null || tags.size() == 0) tagDAO.create(t);
            });

        certificateDAO.create(certificate);
    }
}
