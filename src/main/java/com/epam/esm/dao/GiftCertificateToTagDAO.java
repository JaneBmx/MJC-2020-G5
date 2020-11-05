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

    public int batchInsert(int[][] indexes) {
        int insertsCounts = 0;
        Map<String, Object> parameters = new HashMap<>();

        for (int i = 1; i < indexes.length; i++) {
            parameters.put(GIFT_CERTIFICATE_ID, indexes[i][0]);
            parameters.put(TAG_ID, indexes[i][1]);
            insertsCounts += simpleJdbcInsert.execute(parameters);
        }

        return insertsCounts;
    }

    public int batchDelete(int certificateId) {
        return namedParameterJdbcTemplate.getJdbcTemplate()
                .update(DELETE_INDEXES_QUERY, certificateId);
    }
}
