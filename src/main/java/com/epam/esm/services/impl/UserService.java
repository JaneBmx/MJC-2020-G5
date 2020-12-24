package com.epam.esm.services.impl;

import com.epam.esm.dao.impl.GenericDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.services.ServiceInterface;
import com.epam.esm.util.Criteria;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserService implements ServiceInterface<User> {
    private final GenericDAO<User> userDAO;
    private final GenericDAO<GiftCertificate> certificateDAO;

    @Autowired
    public UserService(GenericDAO<User> userDAO, GenericDAO<GiftCertificate> certificateDAO) {
        this.userDAO = userDAO;
        this.certificateDAO = certificateDAO;
        this.userDAO.setClazz(User.class);
        this.certificateDAO.setClazz(GiftCertificate.class);
    }

    @Override
    @Transactional
    public User getById(int id) {
        return userDAO.getById(id).orElseThrow(()
                -> new ItemNotFoundException(String.format("User id %d not found", id))
        );
    }

    @Override
    @Transactional
    public Pagination<User> getAll(int page, int size, String sort, String sortMode) {
        return userDAO.getAll(new Pagination<>(size, page, 0), new Pair<>(sort, sortMode));
    }

    @Override
    @Transactional
    public Pagination<User> getBy(List<Criteria> criteria, int page, int size, String sort, String sortMode) {
        throw new UnsupportedOperationException("Search operation is not allowed for user");
    }

    @Override
    public User create(User user) {
        throw new UnsupportedOperationException("Create operation for User is not supported");
    }

    @Override
    public User update(User user) {
        throw new UnsupportedOperationException("Update operation for User is not supported");
    }

    @Transactional
    public User addCertificate(GiftCertificate certificate, int id) {
        if (certificate == null || !certificate.equals(certificateDAO.getById(certificate.getId()).orElse(new GiftCertificate()))) {
            throw new ItemNotFoundException("Given certificate not found");
        }
        User user = userDAO.getById(id).orElseThrow(()
                -> new ItemNotFoundException(String.format("User id %d not found", id))
        );

        Order order = new Order(user, certificate, certificate.getPrice(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        user.addOrder(order);

        return userDAO.save(user).orElse(null);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Delete operation for User is not supported.");
    }
}
