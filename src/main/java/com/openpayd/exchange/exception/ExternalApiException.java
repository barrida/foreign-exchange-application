package com.openpayd.exchange.exception;

/**
 * @author suleyman.yildirim
 */
public class ExternalApiException extends BaseException {

    // Constructor with a custom message
    public ExternalApiException(String details) {
        super(ErrorCode.EXTERNAL_API_ERROR, ErrorCode.EXTERNAL_API_ERROR.formatMessage(details));
    }

    // Constructor with a custom message and a cause for the exception
    public ExternalApiException(String details, Throwable cause) {
        super(ErrorCode.EXTERNAL_API_ERROR, ErrorCode.EXTERNAL_API_ERROR.formatMessage(details), cause);
    }
}
