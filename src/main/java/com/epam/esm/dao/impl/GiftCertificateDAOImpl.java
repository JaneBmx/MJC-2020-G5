package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.mappers.CertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.util.Fields.*;

@Repository
public class GiftCertificateDAOImpl extends AbstractDAO<GiftCertificate> implements DAOInterface<GiftCertificate> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM gift_certificate";
    private static final String FIND_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE gift_certificate SET name = ?, description = ?, price = ?, last_update_date = ?, duration = ? WHERE id = ?";
    private static final String CREATE_QUERY = "INSERT INTO gift_certificate (name, description, price, create_date, last_update_date, duration) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM gift_certificate WHERE name = ?";

    @Autowired
    public GiftCertificateDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, new CertificateMapper());
    }

    @Override
    public void create(GiftCertificate certificate) {
        try {
            jdbcTemplate.update(CREATE_QUERY,
                    certificate.getName(),
                    certificate.getDescription(),
                    certificate.getPrice(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    certificate.getDuration());
        } catch (DuplicateKeyException e) {
            throw new DAOException("GiftCertificate with this data already exists!");
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<GiftCertificate> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_QUERY, mapper);
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(GiftCertificate certificate) {
        try {
            jdbcTemplate.update(UPDATE_QUERY,
                    certificate.getName(),
                    certificate.getDescription(),
                    certificate.getPrice(),
                    LocalDateTime.now(),
                    certificate.getDuration(),
                    certificate.getId());
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public int delete(int id) {
        try {
            return jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public GiftCertificate findById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID, mapper, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public GiftCertificate findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, mapper, name);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<GiftCertificate> findBy(Map<String, String> params) {
        try {
            Map<String, Object[]> ready = buildQuery(params);
            for (Map.Entry<String, Object[]> item : ready.entrySet()) {
                return jdbcTemplate.query(item.getKey(), mapper, item.getValue());
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
        return null;
    }

    /**
     * Creating query with query params
     *
     * @param params Map <String, String> where key as search param
     *               value as search value
     *               SORT - search field (name or create_date allowed)
     *               ORDER - result order (ASC or DESC allowed. default = ASC)
     *               NAME - full or part of name for searching
     *               DESCRIPTION - full or part of description for searching
     * @return Map<String, Object [ ]> with key as a query
     * and value as params for query
     */
    private Map<String, Object[]> buildQuery(Map<String, String> params) {
        String sort = params.get(SORT);
        String order = params.getOrDefault(ORDER, "");
        String name = params.getOrDefault(NAME, "");
        String description = params.getOrDefault(DESCRIPTION, "");

        List<String> paramsL = new ArrayList<>();
        paramsL.add("%" + name + "%");
        StringBuilder query = new StringBuilder("SELECT * FROM gift_certificate WHERE name like ? ");

        if (description != null && !description.isEmpty()) {
            query.append(" AND description LIKE ? ");
            paramsL.add("%" + description + "%");
        }

        if (sort != null && !sort.isEmpty()) {
            if (sort.equalsIgnoreCase("name"))
                query.append(" ORDER BY name");
            if (sort.equalsIgnoreCase("create_date"))
                query.append(" ORDER BY create_date ");
        }
        query.append(order);
        Map<String, Object[]> map = new HashMap<>();
        map.put(query.toString(), paramsL.toArray());

        return map;
    }
}
