package com.epam.esm.dao;

import com.epam.esm.entity.baseEntity.BaseEntity;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.util.Criteria;
import javafx.util.Pair;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
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

    public Pagination<T> getBy(List<Criteria> criteria, int page, int size, String sort, String sortMode) {
        StringBuilder queryBuilder = new StringBuilder("from " + clazz.getName() + " t");
        if (!criteria.isEmpty()) {
            queryBuilder.append(" where ");
            criteria.forEach(c -> queryBuilder.
                    append("t.").
                    append(c.getKey()).
                    append(" ").append(c.getOperator().getSign()).append(" ").
                    append(c.getValue()).
                    append(" and "));
            queryBuilder.delete(queryBuilder.lastIndexOf(" and "), queryBuilder.length());
        }

        queryBuilder.
                append(" order by ").
                append(" t.").
                append(sort).
                append(" ").
                append(sortMode);

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setFirstResult(page * size).setMaxResults(size);
        Pagination<T> p = new Pagination<>(size, page, 0);
        p.setContent(query.getResultList());
        queryBuilder.insert(0, "select count(t) ");
        long a =(long) entityManager.createQuery(queryBuilder.toString()).getSingleResult();
        p.setOverallPages(a);

        return p;
    }

    public Optional<T> save(T t) {
        if (t.isNew()) {
            entityManager.persist(t);
            return Optional.of(t);
        }
        return Optional.ofNullable(entityManager.merge(t));
    }

    public void delete(T t) {
        entityManager.remove(t);
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
