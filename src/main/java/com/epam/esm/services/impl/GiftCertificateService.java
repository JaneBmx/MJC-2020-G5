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
        return certificateDAO.getAll(
                new Pagination<>(size, page, 0), new Pair<>(sort, sortMode));
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
    public GiftCertificate update(GiftCertificate newGiftCertificate) {
        if (newGiftCertificate == null || newGiftCertificate.getId() == 0) {
            throw new ItemNotFoundException("Given gift certificate not found.");
        }

        GiftCertificate oldCertificate = certificateDAO.getById(newGiftCertificate.getId()).orElseThrow(()
                -> new ItemNotFoundException("Given gift certificate not found.")
        );

        return certificateDAO.save(validateAndUpdate(oldCertificate, newGiftCertificate)).orElse(null);
    }

    private GiftCertificate validateAndUpdate(GiftCertificate oldGiftCertificate, GiftCertificate newGiftCertificate) {
        boolean costOrDurationChanged = false;
        if (newGiftCertificate.getPrice() == 0) {
            newGiftCertificate.setPrice(oldGiftCertificate.getPrice());
        }

        if (newGiftCertificate.getPrice() != oldGiftCertificate.getPrice()) {
            costOrDurationChanged = true;
        }
        if (newGiftCertificate.getDuration() == 0) {
            newGiftCertificate.setDuration(oldGiftCertificate.getDuration());
        }
        if (newGiftCertificate.getDuration() != oldGiftCertificate.getDuration() && costOrDurationChanged) {
            throw new ServiceException("Price and duration can't be changed at the same time.");
        }
        if (newGiftCertificate.getName() == null || newGiftCertificate.getName().trim().isEmpty()) {
            newGiftCertificate.setName(oldGiftCertificate.getName());
        }
        if (newGiftCertificate.getDescription() == null || newGiftCertificate.getDescription().trim().isEmpty()) {
            newGiftCertificate.setDescription(oldGiftCertificate.getDescription());
        }

        newGiftCertificate.setCreateDate(oldGiftCertificate.getCreateDate());
        newGiftCertificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        //TODO
        // ValidationUtil.validate(oldGiftCertificate);

        return newGiftCertificate;
    }

    @Transactional
    @Override
    public void delete(int id) {
        GiftCertificate certificate = certificateDAO.getById(id).orElseThrow(()
                -> new ItemNotFoundException(String.format("Gift certificate id %d not found", id))
        );

        certificateDAO.delete(certificate);
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
}
