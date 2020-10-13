package com.aredzo.mtracker.sso.dto;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@NotNull
public class TokenResponse {

    @NotNull
    private UUID token;

    @NotNull
    private Instant validBy;

    public TokenResponse() {
    }

    public TokenResponse(@NotNull UUID token, @NotNull Instant validBy) {
        this.token = token;
        this.validBy = validBy;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Instant getValidBy() {
        return validBy;
    }

    public void setValidBy(Instant validBy) {
        this.validBy = validBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenResponse that = (TokenResponse) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(validBy, that.validBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, validBy);
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "token=" + token +
                ", validBy=" + validBy +
                '}';
    }
}
