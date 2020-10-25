package com.epam.esm.dao.mappers;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.util.Fields.*;

public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getInt(ID));
        tag.setName(resultSet.getString(NAME));
        return tag;
    }
}