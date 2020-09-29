package com.aredzo.mtracker.sso.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@NotNull
public class ValidateTokenResponse {

    int userId;

    public ValidateTokenResponse() {
    }

    public ValidateTokenResponse(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidateTokenResponse that = (ValidateTokenResponse) o;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "ValidateTokenResponse{" +
                "userId=" + userId +
                '}';
    }
}
