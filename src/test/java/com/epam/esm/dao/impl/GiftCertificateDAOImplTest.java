package com.epam.esm.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class GiftCertificateDAOImplTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private GiftCertificateDAOImpl giftCertificateDAO;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        giftCertificateDAO = new GiftCertificateDAOImpl(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void create() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("ZhZh");
        giftCertificate.setDescription("zhu zhu");
        giftCertificate.setPrice(3);
        giftCertificate.setDuration(6);

        giftCertificateDAO.create(giftCertificate);
        giftCertificate = giftCertificateDAO.findByName("ZhZh");

        Assertions.assertNotNull(giftCertificate);
        Assertions.assertNotEquals(giftCertificate.getId(), 0);
        Assertions.assertEquals("ZhZh", giftCertificate.getName());
        Assertions.assertEquals("zhu zhu", giftCertificate.getDescription());
        Assertions.assertEquals(3, giftCertificate.getPrice());
        Assertions.assertEquals(6, giftCertificate.getDuration());
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(giftCertificateDAO.findAll());
        Assertions.assertEquals(3, giftCertificateDAO.findAll().size());
    }

    @Test
    void update() {
        GiftCertificate giftCertificate = giftCertificateDAO.findById(1);
        giftCertificate.setName("Zazazazaz");
        giftCertificate.setDescription("Eheheheh");
        giftCertificate.setPrice(2);
        giftCertificate.setDuration(4);
        giftCertificateDAO.update(giftCertificate);

        GiftCertificate changed = giftCertificateDAO.findById(1);
        Assertions.assertNotNull(changed);
        Assertions.assertEquals(changed.getName(),"Zazazazaz" );
        Assertions.assertEquals(changed.getDescription(),"Eheheheh" );
        Assertions.assertEquals(changed.getPrice(), 2);
        Assertions.assertEquals(changed.getDuration(), 4);
    }

    @Test
    void delete() {
        Assertions.assertEquals(1, giftCertificateDAO.delete(1));
        Assertions.assertEquals(0, giftCertificateDAO.delete(999));
    }

    @Test
    void findById() {
        Assertions.assertNotNull(giftCertificateDAO.findById(1));
        Assertions.assertNull(giftCertificateDAO.findById(88));
    }

    @Test
    void findByName() {
        Assertions.assertNotNull(giftCertificateDAO.findByName("Cerf1"));
        Assertions.assertNull(giftCertificateDAO.findByName("NonExistingCertificate"));
    }

    @Test
    void findBy() {

    }
}
