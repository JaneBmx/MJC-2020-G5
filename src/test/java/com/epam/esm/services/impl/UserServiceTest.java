package com.epam.esm.services.impl;

import com.epam.esm.dao.GenericDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.pagination.Pagination;
import javafx.util.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserServiceTest {

    @InjectMocks
    UserService service;

    @Mock
    GenericDAO<User> userGenericDAO;

    @Mock
    GenericDAO<GiftCertificate> giftCertificateGenericDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UserService(userGenericDAO, giftCertificateGenericDAO);
    }

    @Test
    void getById_userExist() {
        User user = new User();
        user.setId(4);
        given(userGenericDAO.getById(4)).willReturn(
                Optional.of(user)
        );

        User returnedUser = service.getById(4);
        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(returnedUser.getId(), user.getId());
    }

    @Test
    void getById_userNotExist() {
        given(userGenericDAO.getById(4)).willReturn(
                Optional.empty()
        );

        Assertions.assertThrows(ItemNotFoundException.class, () -> service.getById(4));
    }

    @Test
    void getAll() {
        Pagination<User> givenPagination = new Pagination<>(20, 1, 0);

        given(userGenericDAO.getAll(givenPagination, new Pair<>("id", "asc")))
                .willReturn(new Pagination<>(new ArrayList<>(), 20, 1, 21));

        Pagination<User> returnedPagination = service.getAll(1, 20, "id", "asc");

        Assertions.assertNotNull(returnedPagination);
        Assertions.assertEquals(givenPagination.getCurrentPage(), 1);
        Assertions.assertEquals(givenPagination.getOverallPages(), 0);
        Assertions.assertEquals(givenPagination.getContent().size(), 0);
        Assertions.assertEquals(givenPagination.getSize(), 20);
    }

    @Test
    void getBy() {
    }

    @Test
    void create() {
        Assertions.assertThrows(UnsupportedOperationException.class, ()
                -> service.create(new User("AAA", "HALP", new HashSet<>())));

    }

    @Test
    void update() {
        Assertions.assertThrows(UnsupportedOperationException.class, ()
                -> service.update(new User(1, "AAA", "HALP", new HashSet<>())));

    }

    @Test
    void delete() {
        Assertions.assertThrows(UnsupportedOperationException.class, ()
                -> service.delete(3));
    }

    @Test
    void addCertificate_withNotExistingCertificate() {
        Assertions.assertThrows(ItemNotFoundException.class, ()
                -> service.addCertificate(null, 3));
    }

    @Test
    void addCertificate_withNotExistingUser() {
        GiftCertificate certificate
                = new GiftCertificate(1, "qwe", "asd", 12, null, null, 12, new HashSet<>());

        given(giftCertificateGenericDAO.getById(1)).willReturn(Optional.of(certificate));
        given(userGenericDAO.getById(1)).willReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class, ()
                -> service.addCertificate(certificate, 1));
    }

    @Test
    void addCertificate() {
        GiftCertificate certificate = new GiftCertificate(1, "qwe", "asd", 12, null, null, 12, new HashSet<>());
        User user = new User(1, "heh", "meh", new HashSet<>());
        given(giftCertificateGenericDAO.getById(1)).willReturn(Optional.of(certificate));
        given(userGenericDAO.getById(1)).willReturn(Optional.of(user));

        user.addOrder(new Order(user, certificate, certificate.getPrice(), null));
        given(userGenericDAO.save(user)).willReturn(
                Optional.of(user));

        User userWithAddedCertificate = service.addCertificate(certificate, user.getId());

        Assertions.assertNotNull(userWithAddedCertificate);
        Assertions.assertFalse(userWithAddedCertificate.getOrders().isEmpty());
        Assertions.assertTrue(user.getOrders().contains(new Order(user, certificate, certificate.getPrice(), null)));
    }
}