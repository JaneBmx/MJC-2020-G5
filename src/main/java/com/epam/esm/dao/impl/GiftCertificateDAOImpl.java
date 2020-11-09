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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    public void delete(int id) {
        try {
            jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
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

    /*@TODO prosnis i napishi*/
    @Override
    public List<GiftCertificate> findBy(Map<String, String> params) {
//        for (Map.Entry<String, MapSqlParameterSource> item : buildQuery(params).entrySet()) {
//            return jdbcTemplate.query(item.getKey(), item.getValue(), mapper);
//        }
        return null;
    }

    private Map<String, MapSqlParameterSource> buildQuery(Map<String, String> params) {
        String sortType = params.get(SORT_TYPE) == null ? SORT_ASC : params.get(SORT_TYPE);
        String name = params.get(NAME) == null ? "" : params.get(NAME);
        MapSqlParameterSource queryParams = new MapSqlParameterSource();

        StringBuilder query = new StringBuilder("SELECT gift_certificate.* FROM gift_certificate");
        if (params.get(SORT) != null) {
            query.append("JOIN gift_certificate2tag ON gift_certificate.id = gift_certificate2tag.gift_certificate_id " +
                    "JOIN tag ON tag.id = gift_certificate2tag.tag_id ");
        }
        query.append(" WHERE gift_certificate.description LIKE :description  " +
                "AND gift_certificate.name LIKE :name ");
        queryParams.addValue(DESCRIPTION, "%" + params.get(DESCRIPTION) + "%")
                .addValue(NAME, "%" + name + "%");
        if (params.get(TAG_NAME) != null) {
            query.append("AND tag.id = (SELECT tag.id FROM tag WHERE tag.name = :tag_name)");
            queryParams.addValue(TAG_NAME, params.get(TAG_NAME));
        }
        if (params.get(SORT) != null) {
            query.append("ORDER BY :sort ").append(":sort_type");
            queryParams.addValue(SORT, params.get(SORT)).addValue(SORT_TYPE, sortType);
        }
        query.append(";");
        Map<String, MapSqlParameterSource> map = new HashMap<>();
        map.put(query.toString(), queryParams);

        return map;
    }
}
