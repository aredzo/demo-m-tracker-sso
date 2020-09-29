package com.aredzo.mtracker.sso.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NotNull
public class UserResponse {

    private int userId;

    @NotEmpty
    private String email;

    public UserResponse() {
    }

    public UserResponse(int userId, @NotEmpty String email) {
        this.userId = userId;
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return userId == that.userId &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }
}
