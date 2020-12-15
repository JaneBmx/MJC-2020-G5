package com.epam.esm.dao;

import com.epam.esm.entity.baseEntity.BaseEntity;
import com.epam.esm.pagination.Pagination;
import javafx.util.Pair;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDAO<T extends BaseEntity> {
    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> clazz;

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Optional<T> getById(int id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    public Pagination<T> getAll(Pagination<T> pagination, Pair<String, String> sortParams) {
        pagination.setContent(
                entityManager.
                        createQuery("from " + clazz.getName() + " t" +
                                " order by t." + sortParams.getKey() + " " + sortParams.getValue()).
                        setFirstResult((pagination.getCurrentPage() - 1) * pagination.getSize()).
                        setMaxResults(pagination.getSize()).
                        getResultList());
        long a = (long) entityManager.createQuery("select count(t) from " + clazz.getName() + " t").getSingleResult();
        pagination.setOverallPages(a);

        return pagination;
    }

    public Pagination<T> getBy(Map<String, Pair<String, String>> filterParams, Pair<String,
            String> sortParams, Pagination<T> p) {
        StringBuilder queryBuilder = new StringBuilder("from " + clazz.getName() + " t");
        if (!filterParams.isEmpty()) {
            queryBuilder.append(" where ");
            filterParams.forEach((k, v) -> queryBuilder.
                    append("t.").
                    append(k).
                    append(v.getValue()).
                    append(v.getKey()).
                    append(" and "));
            queryBuilder.delete(queryBuilder.lastIndexOf(" and "), queryBuilder.length());
        }

        if (sortParams != null) {
            queryBuilder.
                    append(" order by ").
                    append(" t.").
                    append(sortParams.getKey()).
                    append(" ").
                    append(sortParams.getValue());
        }

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setFirstResult(p.getCurrentPage() * p.getSize()).setMaxResults(p.getSize());
        p.setContent(query.getResultList());

        queryBuilder.insert(0, "select count(t) ");
        p.setOverallPages(entityManager.createQuery(queryBuilder.toString()).getFirstResult());

        return p;
    }

    public Optional<T> save(T t) {
        if (t.isNew()) {
            entityManager.persist(t);
            return Optional.ofNullable(t);
        }
        return Optional.ofNullable(entityManager.merge(t));
    }

    public void delete(T t) {
        entityManager.remove(t);
    }
}
