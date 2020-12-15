package com.epam.esm.pagination;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.Objects;

public class RestPagination<T> {
    private final CollectionModel<EntityModel<T>> content;
    private final int size;
    private final int currentPage;
    private final long overallPages;

    public RestPagination(CollectionModel<EntityModel<T>> content, int size, int currentPage, long overallPages) {
        this.content = content;
        this.size = size;
        this.currentPage = currentPage;
        this.overallPages = overallPages;
    }

    public CollectionModel<EntityModel<T>> getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getOverallPages() {
        return overallPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestPagination)) return false;
        RestPagination<?> that = (RestPagination<?>) o;
        return getSize() == that.getSize() &&
                getCurrentPage() == that.getCurrentPage() &&
                getOverallPages() == that.getOverallPages() &&
                Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent(), getSize(), getCurrentPage(), getOverallPages());
    }

    @Override
    public String toString() {
        return "RestPagination{" +
                "content=" + content +
                ", size=" + size +
                ", currentPage=" + currentPage +
                ", overallPages=" + overallPages +
                '}';
    }
}
