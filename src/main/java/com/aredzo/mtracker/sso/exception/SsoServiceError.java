package com.aredzo.mtracker.sso.exception;

public enum SsoServiceError {
    BAD_REQUEST(400, "Bad Request"),
    USER_NOT_FOUND(404, "User Not Found"),
    NOT_AUTHORIZED(403, "User Not Authorized"),
    INTERNAL_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    SsoServiceError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ServiceError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
