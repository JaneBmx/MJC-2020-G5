package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;
import com.epam.esm.entity.base.Person;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "user")
public class User extends Person {
    public User() {
    }

    public User(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public User(String firstName, String lastName, Set<Order> orders) {
        super(firstName, lastName);
        this.orders = orders;
    }

    public User(Integer id, String firstName, String lastName, Set<Order> orders) {
        super(id, firstName, lastName);
        this.orders = orders;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Order> orders = new HashSet<>();

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    public boolean removeOrder(Order order) {
        return this.orders.remove(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;

        return Objects.equals(getOrders(), user.getOrders());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getOrders());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                '}';
    }
}
