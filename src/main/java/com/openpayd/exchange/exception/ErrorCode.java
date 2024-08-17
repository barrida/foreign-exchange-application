package com.openpayd.exchange.exception;

import lombok.Getter;

import java.text.MessageFormat;

/**
 * @author suleyman.yildirim
 * <p>
 * Enum for defining error codes and their associated messages.
 * Messages can include placeholders that will be replaced with dynamic values at runtime.
 */
@Getter
public enum ErrorCode {
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation failed: {0}"),
    EXTERNAL_API_ERROR("EXTERNAL_API_ERROR", "An error occurred while communicating with the external service: {0}"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "{0}");

    private final String code;
    private final String messageTemplate;

    ErrorCode(String code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public String formatMessage(Object... args) {
        return MessageFormat.format(this.messageTemplate, args);
    }
}