package com.epam.esm.dao;

import com.epam.esm.exception.DAOException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GiftCertificateToTagDAO extends AbstractDAO {
    private static final String DELETE_INDEXES_BY_GIFT_CERTIFICATE_QUERY = "DELETE FROM gift_certificate2tag WHERE gift_certificate_id = ?";
    private static final String DELETE_INDEXES_BY_TAG_ID_QUERY = "DELETE FROM gift_certificate2tag WHERE tag_id = ?";
    private static final String CREATE_QUERY = "INSERT INTO gift_certificate2tag (gift_certificate_id, tag_id) VALUES (?, ?)";


    public GiftCertificateToTagDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, null);
    }

    public void mapGiftCertificateToTag(int giftCertificateId, int tagId) {
        try {
            jdbcTemplate.update(CREATE_QUERY, giftCertificateId, tagId);
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    public void deleteByGiftCertificateId(int giftCertificateId) {
        try {
            jdbcTemplate.update(DELETE_INDEXES_BY_GIFT_CERTIFICATE_QUERY, giftCertificateId);
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    public void deleteByTagId(int tagId) {
        try {
            jdbcTemplate.update(DELETE_INDEXES_BY_TAG_ID_QUERY, tagId);
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }
}
