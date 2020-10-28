package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.TagDAO;
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
public class TagDAOImpl extends AbstractDAO<Tag> implements TagDAO {

    @Autowired
    public TagDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super("SELECT * FROM tag",
                "DELETE FROM tag WHERE id = ?",
                null,
                namedParameterJdbcTemplate,
                new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate().getDataSource())
                        .withTableName(TAG_TABLE),
                new TagMapper());
    }

    @Override
    public void create(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, tag.getName());
        simpleJdbcInsert.execute(parameters);
    }

    @Override
    protected Map<String, MapSqlParameterSource> buildQuery(Map<String, String> params) {
        StringBuilder query = new StringBuilder("SELECT * FROM tag");
        MapSqlParameterSource queryParams = new MapSqlParameterSource();
        String sortField = params.remove(SORT);
        String sortType = params.get(SORT_TYPE) == null ? SORT_ASC : params.remove(SORT_TYPE);
        String name = params.remove(NAME);
        String id = params.remove(ID);
        String predicate = " and";

        if (name != null) {
            query.append(String.format(" WHERE %s = :tag_name ", NAME));
            queryParams.addValue("tag_name", name);
        } else predicate = " WHERE";

        if (id != null) {
            query.append(String.format(predicate + " %s = :id ", ID));
            queryParams.addValue(ID, id);
        }

        if (sortField != null && sortField.split(" ").length == 1 && sortType.split(" ").length == 1) //primitive sql-injection protection
            query.append(String.format(" ORDER BY %s %s", sortField, sortType));

        query.append(";");
        Map<String, MapSqlParameterSource> map = new HashMap<>();
        map.put(query.toString(), queryParams);
        System.out.println(query);
        return map;
    }

    @Override
    public List<Tag> getByCertificateId(int certificateId) {
        String query = "SELECT tag.* FROM tag " +
                "LEFT JOIN gift_certificate2tag ON tag.id = gift_certificate2tag.tag_id " +
                "WHERE gift_certificate2tag.gift_certificate_id = " + certificateId + ";";

        return this.namedParameterJdbcTemplate.query(query, this.mapper);
    }
}