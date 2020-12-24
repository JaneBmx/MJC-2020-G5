package com.epam.esm.services.impl;

import com.epam.esm.dao.impl.GenericDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.RequestParamsNotValidException;
import com.epam.esm.pagination.Pagination;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TagServiceTest {

    @InjectMocks
    TagService service;

    @Mock
    GenericDAO<Tag> dao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new TagService(dao);
    }

    @Test
    void getById_tagExist() {
        Tag newTag = new Tag(4, "");
        given(dao.getById(4)).willReturn(
                Optional.of(newTag)
        );

        Tag crearedTag = service.getById(4);

        Assertions.assertNotNull(crearedTag);
        Assertions.assertEquals(newTag.getId(), crearedTag.getId());
    }

    @Test
    void getById_notExistingTag() {
        given(dao.getById(4)).willReturn(
                Optional.empty()
        );

        Assertions.assertThrows(ItemNotFoundException.class, () -> service.getById(4));
    }

    @Test
    void getAll() {
        Pagination<Tag> givenPagination = new Pagination<>(20, 1, 0);

        given(dao.getAll(givenPagination, new Pair<>("id", "asc")))
                .willReturn(new Pagination<>( 20, 1, 21));

        Pagination<Tag> returnedPagination = service.getAll(1, 20, "id", "asc");

        Assertions.assertNotNull(returnedPagination);
        Assertions.assertEquals(givenPagination.getCurrentPage(), 1);
        Assertions.assertEquals(givenPagination.getOverallPages(), 0);
        Assertions.assertEquals(givenPagination.getContent().size(), 0);
        Assertions.assertEquals(givenPagination.getSize(), 20);
    }

    @Test
    void getBy() {
    }

    @Test
    void create() {
        Tag tag = new Tag("Valid name");

        given(dao.save(tag)).willReturn(Optional.of(new Tag(1, "Valid name")));

        Tag created = service.create(tag);
        Assertions.assertNotNull(created);
        Assertions.assertNotEquals(created.getId(), 0);
        Assertions.assertEquals(created.getName(), tag.getName());
    }

    @Test
    void create_withNotValidParams() {
        Assertions.assertThrows(RequestParamsNotValidException.class, () -> service.create(null));

    }

    @Test
    void update() {
        Assertions.assertThrows(UnsupportedOperationException.class, ()
                -> service.update(new Tag(1, "name")));
    }

    @Test
    void delete_notExistingItem() {
        Tag tag = new Tag();
        tag.setId(4);
        given(dao.getById(4)).willReturn(
                Optional.empty()
        );

        Assertions.assertThrows(ItemNotFoundException.class, () -> service.delete(4));

    }
}