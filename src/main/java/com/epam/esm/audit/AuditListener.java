package com.epam.esm.audit;

import com.epam.esm.dao.impl.GenericDAO;
import com.epam.esm.entity.audit.AuditLog;
import com.epam.esm.entity.baseEntity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;
import java.time.LocalDateTime;

@Component
public class AuditListener extends BaseEntity {
    private final GenericDAO<AuditLog> auditDAO;

    @Autowired
    public AuditListener(GenericDAO<AuditLog> auditDAO) {
        this.auditDAO = auditDAO;
        auditDAO.setClazz(AuditLog.class);
    }

    @PostPersist
    private void beforePersist(Object o){
        auditDAO.save(new AuditLog("PERSIST", LocalDateTime.now(), o.getClass().getSimpleName(), ((BaseEntity)o).getId()));
    }

    @PostUpdate
    private void beforeUpdate(Object o){
        auditDAO.save(new AuditLog("UPDATE", LocalDateTime.now(), o.getClass().getSimpleName(), ((BaseEntity)o).getId()));
    }

    @PreRemove
    private void beforeRemove(Object o){
        auditDAO.save(new AuditLog("DELETE", LocalDateTime.now(), o.getClass().getSimpleName(), ((BaseEntity)o).getId()));
    }
}
