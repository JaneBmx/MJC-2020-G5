package com.epam.esm.services.impl;

import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.GiftCertificateToTagDAO;
import com.epam.esm.dao.impl.CertificateDAOImpl;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.services.ServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.util.Fields.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class GiftCertificateServiceImpl implements ServiceInterface<GiftCertificate> {
    private final DAOInterface<GiftCertificate> giftCertificateDAO;
    private final TagDAOImpl tagDAO;
    private final GiftCertificateToTagDAO giftCertificateToTagDAO;

    public GiftCertificateServiceImpl(CertificateDAOImpl giftCertificateDAO,
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
        GiftCertificate giftCertificate = giftCertificateDAO.findById(id);

        List<Tag> tags = tagDAO.findByCertificateId(id);
        giftCertificate.getTags().addAll(tags);

        return giftCertificate;
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

    @Transactional
    @Override
    public void update(GiftCertificate certificate) {
        giftCertificateDAO.update(certificate);

        giftCertificateToTagDAO.batchDelete(certificate.getId());

        for (Tag t : certificate.getTags()) {
            tagDAO.create(t);
        }

        List<Tag> tags = tagDAO.findByCertificateId(certificate.getId());

        int[][] indexes = new int[tags.size()][2];

        for (int i = 0; i < tags.size(); i++) {
            indexes[i][0] = certificate.getId();
            indexes[i][1] = tags.get(i).getId();
        }

        giftCertificateToTagDAO.batchInsert(indexes);
    }

    @Transactional
    @Override
    public void delete(int id) {
        GiftCertificate giftCertificate = giftCertificateDAO.findById(id);
        if (giftCertificate == null)
            throw new GiftCertificateNotFoundException("GiftCertificate with id " + id + " not found!");

        giftCertificateDAO.delete(id);
    }

    @Transactional
    @Override
    public void create(GiftCertificate giftCertificate) {
        giftCertificateDAO.create(giftCertificate);
        for (Tag t : giftCertificate.getTags())
            tagDAO.create(t);

        int[][] indexes = new int[giftCertificate.getTags().size()][2];
        int count = 0;
        for (Tag t : giftCertificate.getTags()) {
            if (t.getId() == 0) {
                tagDAO.create(t);
                t = tagDAO.findByName(t.getName()).get(0);
            }
            indexes[count][0] = giftCertificate.getId();
            indexes[count][1] = t.getId();
            count++;
        }

        giftCertificateToTagDAO.batchInsert(indexes);
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) {
        if (giftCertificate.getId() == 0) {
            create(giftCertificate);

            Map<String, String> params = new HashMap<>();
            params.put(NAME, giftCertificate.getName());
            giftCertificate = giftCertificateDAO.findBy(params).get(0);
            giftCertificate.setTags(new HashSet<Tag>(tagDAO.findByCertificateId(giftCertificate.getId())));

            return giftCertificate;
        } else {
            update(giftCertificate);

            return getById(giftCertificate.getId());
        }
    }
}
