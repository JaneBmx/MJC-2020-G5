package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

public interface CertificateDAO extends DAOInterface<GiftCertificate>{

    public void update(GiftCertificate certificate);
}
