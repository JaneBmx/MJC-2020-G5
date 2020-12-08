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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<GiftCertificate> giftCertificate1 = giftCertificateDAO.findByName("ZhZh");
        Assertions.assertTrue(giftCertificate1.isPresent());
        giftCertificate = giftCertificate1.get();

        Assertions.assertNotNull(giftCertificate);
        Assertions.assertNotEquals(giftCertificate.getId(), 0);
        Assertions.assertEquals("ZhZh", giftCertificate.getName());
        Assertions.assertEquals("zhu zhu", giftCertificate.getDescription());
        Assertions.assertEquals(3, giftCertificate.getPrice());
        Assertions.assertEquals(6, giftCertificate.getDuration());
    }

    @Test
    void findAll() {
        Assertions.assertTrue(giftCertificateDAO.findAll().isPresent());
        Assertions.assertEquals(3, giftCertificateDAO.findAll().get().size());
    }

    @Test
    void update() {
        Optional<GiftCertificate> certificate = giftCertificateDAO.findById(1);
        Assertions.assertTrue(certificate.isPresent());
        GiftCertificate giftCertificate = certificate.get();
        giftCertificate.setName("Zazazazaz");
        giftCertificate.setDescription("Eheheheh");
        giftCertificate.setPrice(2);
        giftCertificate.setDuration(4);
        giftCertificateDAO.update(giftCertificate);

        Optional<GiftCertificate> chngd = giftCertificateDAO.findById(1);
        Assertions.assertTrue(chngd.isPresent());
        GiftCertificate changed = chngd.get();
        Assertions.assertNotNull(changed);
        Assertions.assertEquals(changed.getName(), "Zazazazaz");
        Assertions.assertEquals(changed.getDescription(), "Eheheheh");
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
        Assertions.assertTrue(giftCertificateDAO.findById(1).isPresent());
        Assertions.assertFalse(giftCertificateDAO.findById(88).isPresent());
    }

    @Test
    void findByName() {
        Assertions.assertTrue(giftCertificateDAO.findByName("Cerf1").isPresent());
        Assertions.assertFalse(giftCertificateDAO.findByName("NonExistingCertificate").isPresent());
    }

    @Test
    void findBySortByName() {
        Map<String, String> params = new HashMap<>();
        params.put("SORT", "create_date");
        params.put("ORDER", "ASC");
        params.put("NAME", "Cerf");
        params.put("DESCRIPTION", "desc");

        Optional<List<GiftCertificate>> giftCertificates = giftCertificateDAO.findBy(params);
        Assertions.assertTrue(giftCertificates.isPresent());
        List<GiftCertificate> certificates = giftCertificates.get();

        Assertions.assertNotNull(certificates);
        Assertions.assertEquals(3, certificates.size());

        Assertions.assertEquals(certificates.get(0).getName(), "Cerf1");
        Assertions.assertEquals(certificates.get(0).getDescription(), "desc1");
        Assertions.assertEquals(certificates.get(0).getPrice(), 12.50);
        Assertions.assertEquals(certificates.get(0).getDuration(), 1);
    }
}
