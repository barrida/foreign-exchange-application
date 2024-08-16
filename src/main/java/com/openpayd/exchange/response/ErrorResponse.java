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

    @Schema(name = "Error code", example = "CURRENCY_NOT_FOUND")
    private String errorCode;

    @Schema(name = "Error message", example = "Currency code XXX invalid")
    private String message;

}

