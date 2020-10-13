package com.aredzo.mtracker.sso.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "sso_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId"}),
                @UniqueConstraint(columnNames = {"email"}),
                @UniqueConstraint(columnNames = {"password"})
        }
)
public class SsoUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTypeEnum userType;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SsoTokenEntity> tokens = new ArrayList<>();

    public SsoUserEntity() {
    }

    public SsoUserEntity(String email, String password, Instant validBy) {
        this.email = email;
        this.password = password;
        this.addToken(validBy);
    }

    public SsoUserEntity(String email, String password, UserTypeEnum userType, Instant validBy) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.addToken(validBy);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SsoTokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<SsoTokenEntity> tokens) {
        this.tokens = tokens;
    }

    public SsoTokenEntity addToken(Instant validBy){
        SsoTokenEntity token = new SsoTokenEntity(this, validBy);
        tokens.add(token);
        return token;
    }

    public SsoTokenEntity removeToken(SsoTokenEntity token){
        tokens.remove(token);
        token.setUser(null);
        return token;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "SsoUserEntity{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SsoUserEntity that = (SsoUserEntity) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                userType == that.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, password, userType);
    }

}
