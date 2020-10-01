package com.aredzo.mtracker.sso.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sso_token", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token"})
})
public class SsoTokenEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID token;

    @ManyToOne
    @JoinColumn(name = "userId")
    private SsoUserEntity user;

    @CreationTimestamp
    private Instant createDate = null;

    @Column(nullable = false)
    private Instant validBy;

    public SsoTokenEntity() {
    }

    public SsoTokenEntity(SsoUserEntity user, Instant validBy) {
        this.user = user;
        this.validBy = validBy;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getValidBy() {
        return validBy;
    }

    public void setValidBy(Instant validBy) {
        this.validBy = validBy;
    }

    public SsoUserEntity getUser() {
        return user;
    }

    public void setUser(SsoUserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SsoTokenEntity that = (SsoTokenEntity) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(validBy, that.validBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, createDate, validBy);
    }

    @Override
    public String toString() {
        return "SsoTokenEntity{" +
                "token=" + token +
                ", user=" + user +
                ", createDate=" + createDate +
                ", validBy=" + validBy +
                '}';
    }
}
