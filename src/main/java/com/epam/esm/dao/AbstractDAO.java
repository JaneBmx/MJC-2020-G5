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
}
