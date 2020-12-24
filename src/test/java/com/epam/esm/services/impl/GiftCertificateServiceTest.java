package com.epam.esm.services.impl;

import com.epam.esm.dao.impl.GenericDAO;
import com.epam.esm.dao.impl.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.RequestParamsNotValidException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.pagination.Pagination;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class GiftCertificateServiceTest {
    @InjectMocks
    GiftCertificateService service;

    @Mock
    GiftCertificateDAO dao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new GiftCertificateService(dao);
    }

    @Test
    void getById_certificateExist() {
        GiftCertificate newCertificate = new GiftCertificate();
        newCertificate.setId(4);
        given(dao.getById(4)).willReturn(
                Optional.of(newCertificate)
        );

        GiftCertificate createdCertificate = service.getById(4);
        Assertions.assertNotNull(createdCertificate);
        Assertions.assertEquals(newCertificate.getId(), createdCertificate.getId());
    }

    @Test
    void getById_certificateNotExist() {
        given(dao.getById(4)).willReturn(
                Optional.empty()
        );

        Assertions.assertThrows(ItemNotFoundException.class, () -> service.getById(4));
    }

    @Test
    void getAll() {
        Pagination<GiftCertificate> givenPagination = new Pagination<>(20, 1, 0);

        given(dao.getAll(givenPagination, new Pair<>("id", "asc")))
                .willReturn(new Pagination<>(20, 1, 21));

        Pagination<GiftCertificate> returnedPagination = service.getAll(1, 20, "id", "asc");

        Assertions.assertNotNull(returnedPagination);
        Assertions.assertEquals(givenPagination.getCurrentPage(), 1);
        Assertions.assertEquals(givenPagination.getOverallPages(), 0);
        Assertions.assertEquals(givenPagination.getContent().size(), 0);
        Assertions.assertEquals(givenPagination.getSize(), 20);
    }

    @Test
    void delete_notExistingCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(4);
        given(dao.getById(4)).willReturn(
                Optional.empty()
        );

        Assertions.assertThrows(ItemNotFoundException.class, () -> service.delete(4));
    }

    @Test
    void getBy() {
    }

    @Test
    void create() {
        GiftCertificate giftCertificate = new GiftCertificate("ValidName", "validDescription", 10, null, null, 19, new HashSet<>());
        given(dao.save(giftCertificate)).willReturn(Optional.of(
                new GiftCertificate(
                        1,
                        "ValidName",
                        "validDescription",
                        10,
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        19,
                        new HashSet<>())
        ));
        GiftCertificate created = service.create(giftCertificate);
        Assertions.assertNotNull(created);
        Assertions.assertNotEquals(created.getId(), 0);
        Assertions.assertEquals(giftCertificate.getDescription(), created.getDescription());
        Assertions.assertEquals(giftCertificate.getName(), created.getName());
        Assertions.assertEquals(giftCertificate.getPrice(), created.getPrice());
        Assertions.assertEquals(giftCertificate.getDuration(), created.getDuration());
        Assertions.assertNotNull(created.getCreateDate());
        Assertions.assertNotNull(created.getLastUpdateDate());
    }

    @Test
    void create_withNullCertificate() {
        Assertions.assertThrows(RequestParamsNotValidException.class, () -> service.create(null));
    }

    @Test
    void create_withNotValidParams() {
        Assertions.assertThrows(ServiceException.class, ()
                -> service.create(new GiftCertificate("", "", 0, null, null, 0, null))
        );
    }

    @Test
    void update_noExistingItem() {
        given(dao.getById(5)).willReturn(Optional.empty());

        GiftCertificate giftCertificate = new GiftCertificate(5, "Hate", "writing tests", 228, null, null, 10, new HashSet<>());

        Assertions.assertThrows(ItemNotFoundException.class, ()
                -> service.update(giftCertificate)
        );
    }

    @Test
    void update_notValidParam() {
        Assertions.assertThrows(ItemNotFoundException.class, () -> service.update(null));
    }

    @Test
    void update_bothParamsForUpdate() {
        given(dao.getById(5)).willReturn(Optional.of(
                new GiftCertificate(
                        5,
                        "Halp",
                        "Somebody",
                        10,
                        null,
                        null,
                        10,
                        new HashSet<>()
                )
        ));

        Assertions.assertThrows(ServiceException.class, ()
                -> service.update(new GiftCertificate(
                5,
                "Halp",
                "Somebody",
                15,
                null,
                null,
                15,
                new HashSet<>()
        )));
    }
}