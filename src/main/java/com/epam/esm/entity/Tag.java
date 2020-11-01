package com.epam.esm.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Tag {
    private int id;
    @NotNull
    @Size(min = 1, max = 10)
    private String name;

    public Tag() {
    }

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return id == tag.id &&
                name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}