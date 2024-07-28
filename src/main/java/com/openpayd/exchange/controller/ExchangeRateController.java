package com.openpayd.exchange.controller;

import com.openpayd.exchange.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/exchange-rate")
    public double getExchangeRate(@RequestParam String sourceCurrency, @RequestParam String targetCurrency) throws Exception {
        return exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
    }
}

