package com.epam.esm.audit;

import com.epam.esm.dao.impl.GenericDAO;
import com.epam.esm.entity.audit.AuditLog;
import com.epam.esm.entity.base.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;
import java.time.LocalDateTime;

@Component
public class AuditListener extends BaseEntity {
    private  static final String DELETE_ACTION = "DELETE";
    private  static final String UPDATE_ACTION = "UPDATE";
    private  static final String PERSIST_ACTION = "PERSIST";
    private final GenericDAO<AuditLog> auditDAO;

    @Autowired
    public AuditListener(GenericDAO<AuditLog> auditDAO) {
        this.auditDAO = auditDAO;
        auditDAO.setClazz(AuditLog.class);
    }

    @PostPersist
    private void beforePersist(Object o){
        auditDAO.save(new AuditLog(PERSIST_ACTION, LocalDateTime.now(), o.getClass().getSimpleName(), ((BaseEntity)o).getId()));
    }

    @PostUpdate
    private void beforeUpdate(Object o){
        auditDAO.save(new AuditLog(UPDATE_ACTION, LocalDateTime.now(), o.getClass().getSimpleName(), ((BaseEntity)o).getId()));
    }

    @PreRemove
    private void beforeRemove(Object o){
        auditDAO.save(new AuditLog(DELETE_ACTION, LocalDateTime.now(), o.getClass().getSimpleName(), ((BaseEntity)o).getId()));
    }
}
