package com.openpayd.exchange.exception;

import java.text.MessageFormat;

/**
 * @author suleyman.yildirim
 * <p>
 * Enum for defining error codes and their associated messages.
 * Messages can include placeholders that will be replaced with dynamic values at runtime.
 */
public enum ErrorCode {
    CURRENCY_NOT_FOUND("CURRENCY_NOT_FOUND", "Currency code {0} not found"),
    EXTERNAL_API_ERROR("EXTERNAL_API_ERROR", "An error occurred while communicating with the external service: {0}"),
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation failed: {0}");

    private final String code;
    private final String messageTemplate;

    ErrorCode(String code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public String getCode() {
        return code;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public String formatMessage(Object... args) {
        return MessageFormat.format(this.messageTemplate, args);
    }
}