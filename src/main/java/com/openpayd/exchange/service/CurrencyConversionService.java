package com.openpayd.exchange.service;


import com.openpayd.exchange.exception.ExternalApiException;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public CurrencyConversion convertCurrency(String sourceCurrency, String targetCurrency, BigDecimal amount) throws IOException {
        logger.info("Converting currency from {} to {} for amount {}", sourceCurrency, targetCurrency, amount);
        try {
            final var rate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
            logger.debug("Exchange rate from {} to {} is {}", sourceCurrency, targetCurrency, rate);
            final var convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
            CurrencyConversion conversion = CurrencyConversion.builder()
                    .convertedAmount(convertedAmount)
                    .createdAt(LocalDate.now())
                    .build();
            CurrencyConversion savedConversion = repository.save(conversion);
            logger.info("Saved currency conversion: {}", savedConversion);
            return savedConversion;
        } catch (IOException e) {
            // Add context to the exception and rethrow without logging here
            throw new ExternalApiException(String.format("Currency conversion error from %s to %s: %s", sourceCurrency, targetCurrency, e.getMessage()), e);
        } catch (Exception e) {
            throw new ExternalApiException(String.format("An unexpected error occurred during currency conversion from %s to %s", sourceCurrency, targetCurrency), e);
        }
    }
}

