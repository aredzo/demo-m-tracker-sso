package com.aredzo.mtracker.sso.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@NotNull
public class UserTokenResponse {

    private int userId;

    @NotEmpty
    private String email;

    @NotNull
    private UUID token;

    public UserTokenResponse() {
    }

    public UserTokenResponse(int userId, @NotEmpty String email, @NotNull UUID token) {
        this.userId = userId;
        this.email = email;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTokenResponse that = (UserTokenResponse) o;
        return userId == that.userId &&
                Objects.equals(email, that.email) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, token);
    }

    @Override
    public String toString() {
        return "UserTokenResponse{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", token=" + token +
                '}';
    }
}
