package com.epam.esm.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractDAO<T> {
    protected RowMapper<T> mapper;
    protected JdbcTemplate jdbcTemplate;

    public AbstractDAO(JdbcTemplate jdbcTemplate, RowMapper<T> mapper) {
        this.mapper = mapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public RowMapper<T> getMapper() {
        return mapper;
    }

    public void setMapper(RowMapper<T> mapper) {
        this.mapper = mapper;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
