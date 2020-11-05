package com.epam.esm.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.util.Fields.*;

@Repository
public class GiftCertificateToTagDAO extends AbstractDAO {
    private static final String INSERT_INDEXES_QUERY
            = "INSERT INTO gift_certificate2tag (gift_certificate_id, tag_id) values(?, ?)";
    private static final String DELETE_INDEXES_QUERY =
            "DELETE FROM gift_certificate2tag WHERE gift_certificate_id = ?";

    public GiftCertificateToTagDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(null,
                new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate().getDataSource())
                        .withTableName(TABLE_NAME),
                null);
    }

    public void mapGiftCertificateToTag(int giftCertificateId, int tagId) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(GIFT_CERTIFICATE_ID, giftCertificateId);
        parameters.put(TAG_ID, tagId);
        simpleJdbcInsert.execute(parameters);
    }

    public int batchDelete(int giftCertificateId) {
        return namedParameterJdbcTemplate.getJdbcTemplate()
                .update(DELETE_INDEXES_QUERY, giftCertificateId);
    }
}
