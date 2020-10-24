package com.epam.esm.dao.mappers;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.epam.esm.util.Fields.*;

public class CertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
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
    }
}