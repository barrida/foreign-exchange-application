package com.openpayd.exchange.service;


import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.repository.CurrencyConversionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * @author suleyman.yildirim
 */
@Service
@Setter
@Getter
public class CurrencyConversionService {

    @Value("${currencyapi.api.key}")
    private String apiKey;

    private final ExchangeRateService exchangeRateService;
    private final CurrencyConversionRepository repository;

    @Autowired
    public CurrencyConversionService(ExchangeRateService exchangeRateService, CurrencyConversionRepository repository) {
        this.exchangeRateService = exchangeRateService;
        this.repository = repository;
    }

    @Transactional
    public CurrencyConversion convertCurrency(String sourceCurrency, String targetCurrency, double amount) throws Exception {
        double rate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
        CurrencyConversion conversion = CurrencyConversion.builder()
                .convertedAmount(amount * rate)
                .createdAt(LocalDate.now())
                .build();
        return repository.save(conversion);
    }
}

