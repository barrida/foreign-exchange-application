package com.openpayd.exchange.controller;

import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.response.ErrorResponse;
import com.openpayd.exchange.service.CurrencyConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suleyman.yildirim
 */
@RestController
@RequestMapping("/v1")
public class CurrencyConversionController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    @Autowired
    private final CurrencyConversionService conversionService;

    public CurrencyConversionController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Operation(summary = "Convert currency", description = "Convert a specific amount from one currency to another.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful currency conversion",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyConversion.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/convert-currency")
    public ResponseEntity<CurrencyConversion> convertCurrency(
            @Parameter(description = "Source currency code", required = true, example = "EUR")
            @RequestParam String sourceCurrency,
            @Parameter(description = "Target currency code", required = true, example = "TRY")
            @RequestParam String targetCurrency,
            @Parameter(description = "Amount to convert", required = true,  example = "15.5")
            @RequestParam double amount) {

        logger.info("Received request to convert {} {} to {}", amount, sourceCurrency, targetCurrency);
        var conversion = conversionService.convertCurrency(sourceCurrency, targetCurrency, amount);
        logger.info("Successfully converted {} {} to {} {}", amount, sourceCurrency, conversion.getConvertedAmount(), targetCurrency);
        return ResponseEntity.ok(conversion);
    }
}

