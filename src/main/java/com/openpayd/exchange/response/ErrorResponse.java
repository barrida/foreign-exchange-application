package com.openpayd.exchange.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author suleyman.yildirim
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(name = "Error message", example = "Provide a valid transaction identifier or transaction date")
    private String message;

    @Schema(name = "Error code", example = "INVALID_INPUT")
    private String errorCode;
}

