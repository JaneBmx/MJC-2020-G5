package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;
import com.epam.esm.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    private double cost;

    private String purchaseDate;

    public Order() {
    }

    public Order(User user, GiftCertificate giftCertificate) {
        this.user = user;
        this.giftCertificate = giftCertificate;
        this.cost = giftCertificate.getPrice();
        this.purchaseDate =  LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        user.getOrders().add(this);
    }

    public Order(User user, GiftCertificate giftCertificate, double cost, String purchaseDate) {
        this.user = user;
        this.giftCertificate = giftCertificate;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;

        return Double.compare(order.getCost(), getCost()) == 0 &&
                Objects.equals(getUser(), order.getUser()) &&
                Objects.equals(getGiftCertificate(), order.getGiftCertificate()) &&
                Objects.equals(getPurchaseDate(), order.getPurchaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUser().getId(), getGiftCertificate().getId(), getCost(), getPurchaseDate());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + getId() +
                "user=" + user +
                ", giftCertificate=" + giftCertificate +
                ", cost=" + cost +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
