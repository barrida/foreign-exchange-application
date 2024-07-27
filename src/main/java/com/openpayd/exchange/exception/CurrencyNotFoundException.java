package com.openpayd.exchange.exception;

/**
 * @author suleyman.yildirim
 */
public class CurrencyNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public CurrencyNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }

}
