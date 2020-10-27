package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.mappers.CertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.util.Fields.*;

@Repository
public class CertificateDAOImpl extends AbstractDAO<GiftCertificate> {

    public CertificateDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super("select * from gift_certificate",
                "delete from gift_certificate where id = ?",
                "update gift_certificate set name = ?, description = ?",
                namedParameterJdbcTemplate,
                new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate().getDataSource())
                        .withTableName(CERTIFICATE_TABLE),
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
    public void update(GiftCertificate certificate) {
        Map<String, String> params = new HashMap<>();
        params.put(ID, String.valueOf(certificate.getId()));
        GiftCertificate oldCertificate = this.findBy(params).get(0);
        namedParameterJdbcTemplate.getJdbcTemplate().update(UPDATE_QUERY,
                certificate.getName() == null ? oldCertificate.getName() : certificate.getName(),
                certificate.getDescription() == null ? oldCertificate.getDescription() : certificate.getDescription(),
                certificate.getPrice() == 0.0 ? oldCertificate.getPrice() : certificate.getPrice(),
                certificate.getCreateDate() == null ? oldCertificate.getCreateDate() : certificate.getCreateDate(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                certificate.getDuration() == 0 ? oldCertificate.getDuration() : certificate.getDuration(),
                certificate.getId());
    }

    @Override
    protected Map<String, MapSqlParameterSource> buildQuery(Map<String, String> params) {
        String sortField = params.remove(SORT);
        String sortType = params.get(SORT_TYPE) == null ? SORT_ASC : params.remove(SORT_TYPE);
        String tagName = params.remove(TAG_NAME);
        String name = params.get(NAME) == null ? "" : params.remove(NAME);
        String description = params.get(DESCRIPTION) == null ? "" : params.get(DESCRIPTION);

        MapSqlParameterSource queryParams = new MapSqlParameterSource();

        StringBuilder query = new StringBuilder(
                    "SELECT gift_certificate.* " +
                    "FROM gift_certificate");
        if (tagName != null) {
            query.append(
                    "JOIN gift_certificate2tag " +
                    "ON gift_certificate.id = gift_certificate2tag.gift_certificate_id " +
                    "JOIN tag " +
                    "ON tag.id = gift_certificate2tag.tag_id ");
            queryParams.addValue("tag_name", tagName);
        }
        query.append(
                    "WHERE gift_certificate.description LIKE :description  " +
                    "AND gift_certificate.name LIKE :name ");
        queryParams.addValue("description", "%" + description + "%")
                .addValue("name", "%" + name + "%");
        if (tagName != null) query.append(
                    "AND tag.id = (select tag.id from tag where tag.name = :tag_name)");
        if (sortField != null) {
            query.append(
                    "ORDER BY :sort_field :sort_order");
            queryParams.addValue("sort_field", sortField).addValue("sort_order", sortType);
        }
        query.append(";");

        Map<String, MapSqlParameterSource> map = new HashMap<>();
        map.put(query.toString(), queryParams);
        return map;
    }
}