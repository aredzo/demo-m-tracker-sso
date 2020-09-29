package com.aredzo.mtracker.sso.exception;

public class SsoServiceException extends RuntimeException {

    private final SsoServiceError error;

    public SsoServiceException(SsoServiceError error) {
        this.error = error;
    }

    public SsoServiceError getError() {
        return error;
    }

    @Override
    public String toString() {
        return "SsoServiceException{" +
                "error=" + error +
                '}';
    }
}
