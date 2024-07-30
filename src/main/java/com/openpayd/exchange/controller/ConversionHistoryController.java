package com.openpayd.exchange.controller;

import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.exception.InvalidInputException;
import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.response.ErrorResponse;
import com.openpayd.exchange.service.ConversionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.PastOrPresent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;


/**
 * @author suleyman.yildirim
 */
@RestController
@RequestMapping("/v1")
@Validated
public class ConversionHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(ConversionHistoryController.class);

    private final ConversionHistoryService historyService;

    @Autowired
    public ConversionHistoryController(ConversionHistoryService historyService) {
        this.historyService = historyService;
    }

    @Operation(summary = "Retrieve conversion history by transaction ID and/or date", description = "Provide a transaction ID or date to retrieve the conversion history.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of conversion history", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/conversion-history")
    public ResponseEntity<Page<CurrencyConversion>> getConversionHistory(
            @Parameter(description = "Transaction ID to filter history", example = "71ce75c1-869f-483d-844b-4ab5a3b07eb1")
            @RequestParam(required = false) UUID transactionId,
            @Parameter(description = "Transaction date (YYYY-MM-DD) to filter history", example = "2024-07-30")
            @RequestParam(required = false) @PastOrPresent(message = "Transaction date must be in the past or present")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate,
            Pageable pageable) {

        logger.info("Received request to retrieve conversion history with transactionId: {} and transactionDate: {}", transactionId, transactionDate);

        if (transactionId != null && transactionDate != null) {
            Page<CurrencyConversion> history = historyService.getConversionHistoryByIdAndCreatedAt(transactionId, transactionDate, pageable);
            logger.info("Successfully retrieved conversion history by ID and date for transactionId: {} and transactionDate: {}", transactionId, transactionDate);
            return ResponseEntity.ok(history);
        }

        if (transactionId != null) {
            Page<CurrencyConversion> history = historyService.getConversionHistoryByTransactionId(transactionId, pageable);
            logger.info("Successfully retrieved conversion history by ID for transactionId: {}", transactionId);
            return ResponseEntity.ok(history);
        }

        if (transactionDate != null){
            Page<CurrencyConversion> history = historyService.getConversionHistoryByDate(transactionDate, pageable);
            logger.info("Successfully retrieved conversion history by date for transactionDate: {}", transactionDate);
            return ResponseEntity.ok(history);
        }
        logger.error("Invalid input exception occurred");
        throw new InvalidInputException(ErrorCode.INVALID_INPUT);
    }

}
