package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.DAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.association.GiftCertificate2tag;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.util.Criteria;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDAO extends AbstractDAO<GiftCertificate> implements DAO<GiftCertificate> {
    public Pagination<GiftCertificate> getBy(List<Criteria> criteria, int page, int size, String sort, String sortMode) {
        StringBuilder queryBuilder = new StringBuilder("from " + getClazz().getName() + " t");
        if (!criteria.isEmpty()) {
            queryBuilder.append(" where ");
            Optional<Criteria> tagNameCriteria = Optional.ofNullable(criteria.stream().reduce(null, (acc, c) -> c.getKey().equalsIgnoreCase("tag_name") ? c : acc));
            tagNameCriteria.ifPresent(criteria::remove);
            criteria.forEach(c -> queryBuilder
                    .append("t.")
                    .append(c.getKey())
                    .append(" ").append(c.getOperator().getSign()).append(" ")
                    .append("'").append(c.getValue()).append("'")
                    .append(" and "));
            tagNameCriteria.ifPresent(c ->
                    queryBuilder
                            .append(String.format(" exists " +
                                    "(" +
                                    "    select gct.tag.id " +
                                    "    from %s gct " +
                                    "    join gct.tag gctt " +
                                    "    where t.id = gct.giftCertificate.id and (gctt.name = '%s')" +
                                    ") and ", GiftCertificate2tag.class.getName(), c.getValue())));
            queryBuilder.delete(queryBuilder.lastIndexOf(" and "), queryBuilder.length());
        }

        queryBuilder
                .append(" order by ")
                .append(" t.")
                .append(sort)
                .append(" ")
                .append(sortMode);

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setFirstResult(page * size).setMaxResults(size);
        Pagination<GiftCertificate> pagination = new Pagination<>(size, page, 0);
        pagination.setContent(query.getResultList());
        queryBuilder.insert(0, "select count(t) ");
        long count = (long) entityManager.createQuery(queryBuilder.toString()).getSingleResult();
        pagination.setOverallPages(count);

        return pagination;
    }
}
