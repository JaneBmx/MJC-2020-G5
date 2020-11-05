package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.mappers.CertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.util.Fields.*;

@Repository
public class CertificateDAOImpl extends AbstractDAO<GiftCertificate> implements DAOInterface<GiftCertificate> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM gift_certificate";
    private static final String FIND_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM gift_certificate WHERE id = :id";
    private static final String UPDATE_QUERY = "UPDATE gift_certificate " +
            "SET name = ?, description = ?, price = ?, last_update_date = ?, duration =?";

    @Autowired
    public CertificateDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate,
                new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate().getDataSource()).withTableName(CERTIFICATE_TABLE),
                new CertificateMapper());
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

        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public void update(GiftCertificate certificate) {
        GiftCertificate oldCertificate = this.findById(certificate.getId());
        MapSqlParameterSource queryParams = new MapSqlParameterSource();

        queryParams.addValue(NAME, certificate.getName() == null ? oldCertificate.getName() : certificate.getName())
                .addValue(DESCRIPTION, certificate.getDescription() == null ? oldCertificate.getDescription() : certificate.getDescription())
                .addValue(PRICE, certificate.getPrice() == 0.0 ? oldCertificate.getPrice() : certificate.getPrice())
                .addValue("last_update_date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .addValue(DURATION, certificate.getDuration() == 0 ? oldCertificate.getDuration() : certificate.getDuration());

        namedParameterJdbcTemplate.getJdbcTemplate().update(UPDATE_QUERY, queryParams);
    }

    @Override
    public void delete(int id) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public GiftCertificate findById(int id) {
        MapSqlParameterSource queryParams = new MapSqlParameterSource().addValue(ID, id);

        return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, queryParams, mapper);
    }

    @Override
    public List<GiftCertificate> findByName(String name) {
        Map<String, String>params = new HashMap<>();
        params.put(NAME, name);

        return findBy(params);
    }

    @Override
    public List<GiftCertificate> findBy(Map<String, String> params) {
        for (Map.Entry<String, MapSqlParameterSource> item : buildQuery(params).entrySet()) {
            return namedParameterJdbcTemplate.query(item.getKey(), item.getValue(), mapper);
        }
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
