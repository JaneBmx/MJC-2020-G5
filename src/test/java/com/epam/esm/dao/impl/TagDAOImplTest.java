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

import java.util.List;

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
        tag = tagDAO.findByName("SixthTag");

        Assertions.assertNotNull(tag);
        Assertions.assertNotEquals(tag.getId(), 0);
        Assertions.assertEquals("SixthTag", tag.getName());
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(tagDAO.findAll());
        Assertions.assertEquals(5, tagDAO.findAll().size());
    }

    @Test
    void findBy() {
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
        List<Tag> tags = tagDAO.findByCertificateId(1);
        Assertions.assertNotNull(tags);
        Assertions.assertEquals(2, tags.size());
    }

    @Test
    void findById() {
        Assertions.assertNotNull(tagDAO.findById(1));
        Assertions.assertNull(tagDAO.findById(88));
    }

    @Test
    void findByName() {
        Assertions.assertNotNull(tagDAO.findByName("FirstTag"));
        Assertions.assertNull(tagDAO.findByName("NonExistingTag"));
    }
}
