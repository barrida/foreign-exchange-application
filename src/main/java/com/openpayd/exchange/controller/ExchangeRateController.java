package com.openpayd.exchange.controller;

import com.openpayd.exchange.response.ErrorResponse;
import com.openpayd.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author suleyman.yildirim
 */
@RestController
@RequestMapping("/v1")
@Validated
public class ExchangeRateController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);

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
    public ResponseEntity<BigDecimal> getExchangeRate(
            @Parameter(description = "Source currency code. USD is set as a default base currency", required = true, example = "EUR")
            @RequestParam (defaultValue = "USD") String sourceCurrency,
            @Parameter(description = "Target currency code", required = true, example = "GBP")
            @RequestParam @NotEmpty String targetCurrency) throws Exception {

        logger.info("Received request to get exchange rate from {} to {}", sourceCurrency, targetCurrency);
        var rate= exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
        logger.info("Exchange rate from {} to {} is {}", sourceCurrency, targetCurrency, rate);
        return ResponseEntity.ok(rate);
    }
}

