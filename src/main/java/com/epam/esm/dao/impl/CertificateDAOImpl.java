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

@Component
public class CertificateDAOImpl implements CertificateDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertCertificate;

    @Autowired
    public CertificateDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertCertificate = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("gift_certificate");
    }

    @Override
    public void create(GiftCertificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", certificate.getName());
        parameters.put("description", certificate.getDescription());
        parameters.put("price", certificate.getPrice());
        parameters.put("create_date", certificate.getCreateDate());
        parameters.put("last_update_date", certificate.getLastUpdateDate());
        parameters.put("duration", certificate.getDuration());

        insertCertificate.execute(parameters);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query("select * from gift_certificate", (resultSet, i) -> {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(resultSet.getInt("id"));
            certificate.setName(resultSet.getString("name"));
            certificate.setDescription(resultSet.getString("description"));
            certificate.setPrice(resultSet.getDouble("price"));
            certificate.setLastUpdateDate(new Timestamp(resultSet.getTimestamp("last_update_date")
                    .getTime())
                    .toLocalDateTime());
            certificate.setCreateDate(new Timestamp(resultSet.getTimestamp("create_date").getTime())
                    .toLocalDateTime());
            certificate.setId(resultSet.getInt("id"));

            return certificate;
        });
    }

    @Override
    public List<GiftCertificate> findBy(Map<String, String> params) {
        return null;
    }

    @Override
    public void delete(GiftCertificate certificate) {
        jdbcTemplate.update("delete from gift_certificate where id = ?", certificate.getId());
    }

    @Override
    public void update(GiftCertificate certificate) {
        jdbcTemplate.update("update gift_certificate set name = ?, description = ?, price = ?, create_date = ?, " +
                        "last_update_date = ?,description = ? where id = ?",
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                certificate.getDuration(),
                certificate.getId());
    }
}