package com.openpayd.exchange.exception;

/**
 * @author suleyman.yildirim
 */
public class CurrencyNotFoundException extends BaseException {

    public CurrencyNotFoundException(String currencyCode) {
        super(ErrorCode.CURRENCY_NOT_FOUND, ErrorCode.CURRENCY_NOT_FOUND.formatMessage(currencyCode));
    }

    public CurrencyNotFoundException(String currencyCode, Throwable cause) {
        super(ErrorCode.CURRENCY_NOT_FOUND, ErrorCode.CURRENCY_NOT_FOUND.formatMessage(currencyCode), cause);
    }
}
