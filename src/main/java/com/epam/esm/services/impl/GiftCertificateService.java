package com.epam.esm.services.impl;

import com.epam.esm.dao.GenericDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.RequestParamsNotValidException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.services.ServiceInterface;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class GiftCertificateService implements ServiceInterface<GiftCertificate> {
    private final GenericDAO<GiftCertificate> certificateDAO;

    @Autowired
    public GiftCertificateService(GenericDAO<GiftCertificate> certificateDAO) {
        this.certificateDAO = certificateDAO;
        this.certificateDAO.setClazz(GiftCertificate.class);
    }

    @Transactional
    @Override
    public GiftCertificate getById(int id) {
        return certificateDAO.getById(id).orElseThrow(()
                -> new ItemNotFoundException(String.format("Gift certificate id %d not found", id))
        );
    }

    @Transactional
    @Override
    public Pagination<GiftCertificate> getAll(int page, int size, String sort, String sortMode) {
        return certificateDAO.getAll(new Pagination<>(size, page, 0), new Pair<>(sort, sortMode));
    }

    @Transactional
    @Override
    public Pagination<GiftCertificate> getBy(Map<String, Pair<String, String>> filterParams,
                                             Pair<String, String> sortParams, int page, int size) {
        //TODO
        return null;
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        if (giftCertificate == null) {
            throw new RequestParamsNotValidException("Empty body");
        }

        if (giftCertificate.getName() == null || giftCertificate.getName().trim().isEmpty()
                || giftCertificate.getDescription() == null || giftCertificate.getDescription().trim().isEmpty()
                || giftCertificate.getDuration() == 0 || giftCertificate.getPrice() == 0) {
            throw new ServiceException("Name, description, duration, price required");
        }
        giftCertificate.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        giftCertificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        return certificateDAO.save(giftCertificate).orElse(null);
    }

    @Transactional
    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        if (giftCertificate == null || giftCertificate.getId() == 0) {
            throw new ItemNotFoundException("Given gift certificate not found.");
        }

        GiftCertificate oldCertificate = certificateDAO.getById(giftCertificate.getId()).orElseThrow(()
        ->new ItemNotFoundException("Given gift certificate not found.")
        );

        boolean costOrDurationChanged = false;
        if (giftCertificate.getPrice() == 0) {
            giftCertificate.setPrice(oldCertificate.getPrice());
        }
        if (giftCertificate.getPrice() != oldCertificate.getPrice()) {
            costOrDurationChanged = true;
        }
        if (giftCertificate.getDuration() == 0) {
            giftCertificate.setDuration(oldCertificate.getDuration());
        }
        if (giftCertificate.getDuration() != oldCertificate.getDuration() && costOrDurationChanged) {
            throw new ServiceException("Price and duration can't be changed at the same time.");
        }
        if (giftCertificate.getName() == null || giftCertificate.getName().trim().isEmpty()) {
            giftCertificate.setName(oldCertificate.getName());
        }
        if (giftCertificate.getDescription() == null || giftCertificate.getDescription().trim().isEmpty()) {
            giftCertificate.setDescription(oldCertificate.getDescription());
        }

        giftCertificate.setCreateDate(oldCertificate.getCreateDate());
        giftCertificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        return certificateDAO.save(giftCertificate).orElse(null);
    }

    @Transactional
    @Override
    public void delete(int id) {
        GiftCertificate certificate = certificateDAO.getById(id).orElseThrow(()
                -> new ItemNotFoundException(String.format("Gift certificate id %d not found", id))
        );

        certificateDAO.delete(certificate);
    }
}
