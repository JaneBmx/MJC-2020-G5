package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.mappers.TagMapper;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.util.Fields.*;

@Repository
public class TagDAOImpl extends AbstractDAO<Tag> implements DAOInterface<Tag> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM tag";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM tag WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM tag WHERE NAME LIKE :name";
    private static final String FIND_BY_TAG_ID_QUERY = "SELECT * FROM tag WHERE id = :id";
    private static final String FIND_BY_CERTIFICATE_ID_QUERY = "SELECT id, name FROM tag t " +
            "LEFT JOIN gift_certificate2tag g " +
            "ON t.id = g.tag_id WHERE gift_certificate_id = :id;";

    @Autowired
    public TagDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate,
                new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate().getDataSource()).withTableName(TAG_TABLE),
                new TagMapper());
    }

    @Override
    public void create(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, tag.getName());
        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public List<Tag> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public List<Tag> findBy(Map<String, String> params) {
        throw new UnsupportedOperationException("findBy don't support");
    }

    @Override
    public void update(Tag tag) {
        throw new UnsupportedOperationException("update don't support");
    }

    @Override
    public void delete(int id) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(DELETE_BY_ID_QUERY, id);
    }

    public List<Tag> findByCertificateId(int id) {
        MapSqlParameterSource queryParams = new MapSqlParameterSource().addValue(ID, id);
        return namedParameterJdbcTemplate.query(FIND_BY_CERTIFICATE_ID_QUERY, queryParams, mapper);
    }

    @Override
    public Tag findById(int id) {
        MapSqlParameterSource queryParams = new MapSqlParameterSource().addValue(ID, id);
        return namedParameterJdbcTemplate.queryForObject(FIND_BY_TAG_ID_QUERY, queryParams, mapper);
    }

    @Override
    public List<Tag> findByName(String name) {
        MapSqlParameterSource queryParams = new MapSqlParameterSource()
                .addValue(NAME, "%" + name + "%");
        return namedParameterJdbcTemplate.query(FIND_BY_NAME_QUERY, queryParams, mapper);
    }
}
