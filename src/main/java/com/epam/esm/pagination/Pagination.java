package com.epam.esm.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pagination<T> {
    private List<T> content = new ArrayList<>();
    private int size;
    private int currentPage;
    private long overallPages;

    public Pagination(int size, int currentPage, long overallRows) {
        this.size = size;
        this.currentPage = currentPage;
        this.overallPages = overallRows / size;
        if (overallRows % size != 0) {
            this.overallPages++;
        }
    }

    public Pagination(List<T> content, int size, int currentPage, long overallRows) {
        this.size = size;
        this.content = content;
        this.currentPage = currentPage;
        this.overallPages = overallRows / size;
        if (overallRows % size != 0){
            this.overallPages++;
        }
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getOverallPages() {
        return overallPages;
    }

    public void setOverallPages(long overallRows) {
        this.overallPages = overallRows / size;
        if (overallRows % size != 0) {
            this.overallPages++;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pagination)) return false;
        Pagination<T> that = (Pagination<T>) o;

        return getSize() == that.getSize() &&
                getCurrentPage() == that.getCurrentPage() &&
                getOverallPages() == that.getOverallPages() &&
                getContent().equals(that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSize(), getContent(), getCurrentPage(), getOverallPages());
    }

    @Override
    public String toString() {
        return "SuperExclusivePagination{" + "content=" + content +
                ", size=" + size +
                ", currentPage=" + currentPage +
                ", totalPages=" + overallPages +
                '}';
    }
}
