package com.epam.esm.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class AbstractDAO<T>{
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SimpleJdbcInsert simpleJdbcInsert;
    protected RowMapper<T> mapper;

    public AbstractDAO(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            SimpleJdbcInsert simpleJdbcInsert,
            RowMapper<T> mapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
        this.mapper = mapper;
    }
}
