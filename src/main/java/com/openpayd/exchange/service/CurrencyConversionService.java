package com.openpayd.exchange.service;


import com.openpayd.exchange.exception.CurrencyNotFoundException;
import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.repository.CurrencyConversionRepository;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);

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
        logger.info("Converting currency from {} to {} for amount {}", sourceCurrency, targetCurrency, amount);
        try {
            double rate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
            logger.debug("Exchange rate from {} to {} is {}", sourceCurrency, targetCurrency, rate);
            CurrencyConversion conversion = CurrencyConversion.builder()
                    .convertedAmount(amount * rate)
                    .createdAt(LocalDate.now())
                    .build();
            CurrencyConversion savedConversion = repository.save(conversion);
            logger.info("Saved currency conversion: {}", savedConversion);
            return savedConversion;
        } catch (Exception e) {
            logger.error("Error occurred during currency conversion from {} to {}", sourceCurrency, targetCurrency, e);
            throw new CurrencyNotFoundException(ErrorCode.CURRENCY_NOT_FOUND);
        }
    }
}

