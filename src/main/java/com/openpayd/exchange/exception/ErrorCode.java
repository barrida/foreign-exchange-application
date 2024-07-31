package com.openpayd.exchange.exception;

/**
 * @author suleyman.yildirim
 */
public enum ErrorCode {
    CURRENCY_NOT_FOUND("CURRENCY_NOT_FOUND", "Currency code invalid or not supported by the service provider"),
    INVALID_INPUT("INVALID_INPUT", "Provide a valid transaction identifier or transaction date");

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

