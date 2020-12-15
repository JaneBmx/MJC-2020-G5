package com.epam.esm.entity;

import com.epam.esm.entity.baseEntity.NamedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "gift_certificate")
public class GiftCertificate extends NamedEntity {
    private String description;

    private double price;

    private String createDate;

    private String lastUpdateDate;

    private int duration;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "gift_certificate2tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public GiftCertificate() {
    }

    public GiftCertificate(String name, String description, double price, int duration) {
        super(name);
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.lastUpdateDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.tags = new HashSet<>();
    }

    public GiftCertificate(String name, String description, double price, String createDate, String lastUpdateDate,
                           int duration, Set<Tag> tags) {
        super(name);
        this.description = description;
        this.price = price;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.tags = tags == null ? new HashSet<>() : tags;
    }

    public GiftCertificate(Integer id, String name, String description, double price, String createDate,
                           String lastUpdateDate, int duration, Set<Tag> tags) {
        super(id, name);
        this.description = description;
        this.price = price;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.tags = tags == null ? new HashSet<>() : tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate)) return false;
        if (!super.equals(o)) return false;
        GiftCertificate that = (GiftCertificate) o;

        return Double.compare(that.getPrice(), getPrice()) == 0 &&
                getDuration() == that.getDuration() &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCreateDate(), that.getCreateDate()) &&
                Objects.equals(getLastUpdateDate(), that.getLastUpdateDate()) &&
                Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription(), getPrice(), getCreateDate(), getLastUpdateDate(),
                getDuration(), getTags());
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createDate='" + createDate + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", duration=" + duration +
                ", tags=" + tags +
                '}';
    }
}
