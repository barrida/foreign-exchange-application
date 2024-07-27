package com.openpayd.exchange.exception;

/**
 * @author suleyman.yildirim
 */
public enum ErrorCode {
    CURRENCY_NOT_FOUND("CURRENCY_NOT_FOUND", "Invalid currency code or response from API"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

