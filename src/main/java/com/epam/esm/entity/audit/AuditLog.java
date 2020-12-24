package com.epam.esm.entity.audit;

import com.epam.esm.entity.baseEntity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "audit_log")
public class AuditLog extends BaseEntity {
    private String operation;

    @Column(columnDefinition="TIMESTAMP")
    private LocalDateTime timestamp;
    private String clazz;
    private Integer entityId;

    public AuditLog() {
    }

    public AuditLog(String operation, LocalDateTime timestamp, String clazz, Integer entityId) {
        this.operation = operation;
        this.timestamp = timestamp;
        this.clazz = clazz;
        this.entityId=entityId;
    }

    public String getOperation() {
        return operation;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getClazz() {
        return clazz;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditLog)) return false;
        if (!super.equals(o)) return false;
        AuditLog auditLog = (AuditLog) o;
        return getTimestamp() == auditLog.getTimestamp() &&
                Objects.equals(getOperation(), auditLog.getOperation()) &&
                Objects.equals(getClazz(), auditLog.getClazz()) &&
                Objects.equals(getEntityId(), auditLog.getEntityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getOperation(), getTimestamp(), getClazz(), getEntityId());
    }
}
