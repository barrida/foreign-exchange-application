package com.openpayd.exchange.controller;

import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.service.CurrencyConversionService;
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

    @Autowired
    private final CurrencyConversionService conversionService;

    public CurrencyConversionController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @PostMapping("/convert-currency")
    public ResponseEntity<CurrencyConversion> convertCurrency(
            @RequestParam String sourceCurrency,
            @RequestParam String targetCurrency,
            @RequestParam double amount) throws Exception {
        CurrencyConversion conversion = conversionService.convertCurrency(sourceCurrency, targetCurrency, amount);
        return ResponseEntity.ok(conversion);
    }
}

