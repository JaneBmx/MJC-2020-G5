package com.epam.esm.entity;

import com.epam.esm.entity.baseEntity.NamedEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag extends NamedEntity {
    public Tag() {
    }

    public Tag(String name) {
        super(name);
    }

    public Tag(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                "}";
    }
}
