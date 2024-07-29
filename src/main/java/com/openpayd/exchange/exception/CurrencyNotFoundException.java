package com.openpayd.exchange.exception;

/**
 * @author suleyman.yildirim
 */
public class CurrencyNotFoundException extends BaseException {

    public CurrencyNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
