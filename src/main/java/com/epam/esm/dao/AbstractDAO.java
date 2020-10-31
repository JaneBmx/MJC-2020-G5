package com.epam.esm.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Map;

public abstract class AbstractDAO<T> implements DAOInterface<T> {
    protected final String FIND_ALL_QUERY;
    protected final String DELETE_QUERY;
    protected final String UPDATE_QUERY;

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SimpleJdbcInsert simpleJdbcInsert;
    protected RowMapper<T> mapper;

    public AbstractDAO(String FIND_ALL_QUERY, String DELETE_QUERY, String UPDATE_QUERY, NamedParameterJdbcTemplate namedParameterJdbcTemplate, SimpleJdbcInsert simpleJdbcInsert, RowMapper<T> mapper) {
        this.FIND_ALL_QUERY = FIND_ALL_QUERY;
        this.DELETE_QUERY = DELETE_QUERY;
        this.UPDATE_QUERY = UPDATE_QUERY;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
        this.mapper = mapper;
    }

    @Override
    public List<T> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public void delete(int id) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(DELETE_QUERY, id);
    }

    @Override
    public List<T> findBy(Map<String, String> params) {
        for (Map.Entry<String, MapSqlParameterSource> item : buildQuery(params).entrySet()) {
            return namedParameterJdbcTemplate.query(item.getKey(), item.getValue(), mapper);
        }
        return null;
    }

    /**
     * Makes query depending by the search parameters
     *
     * @param params Map<key, value>  with search criteria as key and search parameter as value
     * @return Map<key, value> with query as key and parameters for this query as value
     * @see Map
     */
    protected abstract Map<String, MapSqlParameterSource> buildQuery(Map<String, String> params);
}