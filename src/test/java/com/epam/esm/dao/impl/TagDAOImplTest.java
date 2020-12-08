package com.epam.esm.dao.impl;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

class TagDAOImplTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private TagDAOImpl tagDAO;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        tagDAO = new TagDAOImpl(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void create() {
        Tag tag = new Tag();
        tag.setName("SixthTag");
        tagDAO.create(tag);
        Optional<Tag>tg = tagDAO.findByName("SixthTag");
        Assertions.assertTrue(tg.isPresent());
        tag = tg.get();
        Assertions.assertNotNull(tag);
        Assertions.assertNotEquals(tag.getId(), 0);
        Assertions.assertEquals("SixthTag", tag.getName());
    }

    @Test
    void findAll() {
        Assertions.assertTrue(tagDAO.findAll().isPresent());
        Assertions.assertEquals(5, tagDAO.findAll().get().size());
    }

    @Test
    void findBy() {
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                tagDAO.findBy(new HashMap<>()));
    }

    @Test()
    void update() {
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                tagDAO.update(new Tag(1, "Oleg")));
    }

    @Test
    void delete() {
        Assertions.assertEquals(1, tagDAO.delete(4));
        Assertions.assertEquals(0, tagDAO.delete(999));
    }

    @Test
    void findByCertificateId() {
        Optional<List<Tag>> tags = tagDAO.findByCertificateId(1);
        Assertions.assertTrue(tags.isPresent());
        Assertions.assertNotNull(tags);
        Assertions.assertEquals(2, tags.get().size());
    }

    @Test
    void findById() {
        Assertions.assertTrue(tagDAO.findById(1).isPresent());
        Assertions.assertFalse(tagDAO.findById(88).isPresent());
    }

    @Test
    void findByName() {
        Assertions.assertTrue(tagDAO.findByName("FirstTag").isPresent());
        Assertions.assertFalse(tagDAO.findByName("NonExistingTag").isPresent());
    }
}
