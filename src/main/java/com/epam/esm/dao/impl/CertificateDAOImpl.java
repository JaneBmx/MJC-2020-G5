package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.util.Fields.*;

@Component
public class CertificateDAOImpl implements CertificateDAO {
    private static final String DELETE_QUERY = "delete from gift_certificate where id = ?";
    private static final String UPDATE_QUERY = "update gift_certificate set name = ?, description = ?, price = ?, " +
            "create_date = ?, last_update_date = ?,description = ? where id = ?";
    private static final String FIND_ALL_QUERY = "select * from gift_certificate";


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertCertificate;

    @Autowired
    public CertificateDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertCertificate = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName(CERTIFICATE_TABLE);
    }

    @Override
    public void create(GiftCertificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, certificate.getName());
        parameters.put(DESCRIPTION, certificate.getDescription());
        parameters.put(PRICE, certificate.getPrice());
        parameters.put(CREATE_DATE, certificate.getCreateDate());
        parameters.put(LAST_UPDATE_DATE, certificate.getLastUpdateDate());
        parameters.put(DURATION, certificate.getDuration());

        insertCertificate.execute(parameters);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, (resultSet, i) -> {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(resultSet.getInt(ID));
            certificate.setName(resultSet.getString(NAME));
            certificate.setDescription(resultSet.getString(DESCRIPTION));
            certificate.setPrice(resultSet.getDouble(PRICE));
            certificate.setLastUpdateDate(new Timestamp(resultSet.getTimestamp(LAST_UPDATE_DATE)
                    .getTime())
                    .toLocalDateTime());
            certificate.setCreateDate(new Timestamp(resultSet.getTimestamp(CREATE_DATE).getTime())
                    .toLocalDateTime());
            certificate.setId(resultSet.getInt(ID));

            return certificate;
        });
    }

    @Override
    public List<GiftCertificate> findBy(Map<String, String> params) {
        return null;
    }

    @Override
    public void delete(GiftCertificate certificate) {
        jdbcTemplate.update(DELETE_QUERY, certificate.getId());
    }

    @Override
    public void update(GiftCertificate certificate) {
        jdbcTemplate.update(UPDATE_QUERY,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                certificate.getDuration(),
                certificate.getId());
    }
}