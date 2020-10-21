package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TagDAOImpl implements TagDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertTag;

    @Autowired
    public TagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertTag = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("tag");
    }

    @Override
    public void create(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", tag.getName());

        insertTag.execute(parameters);
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query("select * from tag", (resultSet, i) -> {
            Tag tag = new Tag();
            tag.setId(resultSet.getInt("id"));
            tag.setName(resultSet.getString("name"));
            return tag;
        });
    }

    @Override
    public List<Tag> findBy(String criteria, String value) {
        return null;
    }

    @Override
    public void delete(Tag tag) {
        jdbcTemplate.update("delete from tag where id = ?", tag.getId());
    }
}