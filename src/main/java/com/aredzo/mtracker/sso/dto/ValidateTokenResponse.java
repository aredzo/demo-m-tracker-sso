package com.aredzo.mtracker.sso.dto;

import com.aredzo.mtracker.sso.entity.UserTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@NotNull
public class ValidateTokenResponse {

    private int userId;

    private UserTypeEnum userType;

    public ValidateTokenResponse() {
    }

    public ValidateTokenResponse(int userId, UserTypeEnum userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidateTokenResponse that = (ValidateTokenResponse) o;
        return userId == that.userId &&
                userType == that.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userType);
    }

    @Override
    public String toString() {
        return "ValidateTokenResponse{" +
                "userId=" + userId +
                ", userType=" + userType +
                '}';
    }
}
