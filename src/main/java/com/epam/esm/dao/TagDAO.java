package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDAO extends DAOInterface<Tag> {

    List<Tag> getByCertificateId(int certificateId);
}