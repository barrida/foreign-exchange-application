package com.openpayd.exchange.controller;

import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.exception.InvalidInputException;
import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.service.ConversionHistoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
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

    private final ConversionHistoryService historyService;

    @Autowired
    public ConversionHistoryController(ConversionHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/conversion-history")
    public ResponseEntity<Page<CurrencyConversion>> getConversionHistory(
            @RequestParam(required = false) @Valid UUID transactionId,
            @RequestParam(required = false)
            @PastOrPresent(message = "Transaction date must be in the past or present")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate,
            Pageable pageable) {

        if (transactionId != null && transactionDate != null) {
            return ResponseEntity.ok(historyService.getConversionHistoryByIdAndCreatedAt(transactionId, transactionDate, pageable));
        }
        if (transactionId != null)
            return ResponseEntity.ok(historyService.getConversionHistoryByTransactionId(transactionId, pageable));

        if (transactionDate != null)
            return ResponseEntity.ok(historyService.getConversionHistoryByDate(transactionDate, pageable));

        throw new InvalidInputException(ErrorCode.INVALID_INPUT);
    }

}
