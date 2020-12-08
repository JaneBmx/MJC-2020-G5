package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.DAOInterface;
import com.epam.esm.dao.mappers.TagMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagDAOImpl extends AbstractDAO<Tag> implements DAOInterface<Tag> {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM tag";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM tag WHERE id = ? ";
    private static final String FIND_BY_NAME_QUERY = "SELECT id, name FROM tag WHERE name = ?";
    private static final String FIND_BY_TAG_ID_QUERY = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_BY_CERTIFICATE_ID_QUERY = "SELECT id, name FROM tag t LEFT JOIN gift_certificate2tag g ON t.id = g.tag_id WHERE gift_certificate_id = ?;";
    private static final String CREATE_QUERY = "INSERT into tag (name) values (?)";

    @Autowired
    public TagDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, new TagMapper());
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        try {
            jdbcTemplate.update(CREATE_QUERY, tag.getName());

            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, mapper, tag.getName()));
        } catch (DuplicateKeyException e) {
            throw new DAOException("Tag with this data already exists!");
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<List<Tag>> findAll() {
        try {
            return Optional.of(jdbcTemplate.query(FIND_ALL_QUERY, mapper));
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<List<Tag>> findBy(Map<String, String> params) {
        throw new UnsupportedOperationException("FindBy operation not supporting");
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        throw new UnsupportedOperationException("Update operation not supporting");
    }

    @Override
    public int delete(int id) {
        try {
            return jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    public Optional<List<Tag>> findByCertificateId(int id) {
        try {
            return Optional.of(jdbcTemplate.query(FIND_BY_CERTIFICATE_ID_QUERY, mapper, id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Tag> findById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_TAG_ID_QUERY, mapper, id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, mapper, name));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }
}
