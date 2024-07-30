package com.openpayd.exchange.controller;

import com.openpayd.exchange.response.ErrorResponse;
import com.openpayd.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suleyman.yildirim
 */
@RestController
@RequestMapping("/v1")
public class ExchangeRateController {

    @Autowired
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Operation(summary = "Get exchange rate", description = "Retrieve the current exchange rate for a given currency pair.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of exchange rate",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/exchange-rate")
    public ResponseEntity<Double> getExchangeRate(
            @Parameter(description = "Source currency code", required = true, example = "EUR")
            @RequestParam String sourceCurrency,
            @Parameter(description = "Target currency code", required = true, example = "GBP")
            @RequestParam String targetCurrency) throws Exception {
        double rate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
        return ResponseEntity.ok(rate);
    }
}

