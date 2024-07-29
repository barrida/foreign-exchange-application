package com.openpayd.exchange.exception;

/**
 * @author suleyman.yildirim
 */
public class InvalidInputException extends BaseException {

    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode);
    }
}
