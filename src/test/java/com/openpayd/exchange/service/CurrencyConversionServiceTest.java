package com.openpayd.exchange.service;

import com.openpayd.exchange.exception.CurrencyNotFoundException;
import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.repository.CurrencyConversionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author suleyman.yildirim
 */

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@ExtendWith(MockitoExtension.class)
class CurrencyConversionServiceTest {

    @Mock
    private CurrencyConversionRepository repository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private CurrencyConversionService conversionService;

    @Value("${currencyapi.api.key}")
    private String apiKey;

    @BeforeEach
    public void setUp() {
        conversionService = new CurrencyConversionService(exchangeRateService, repository);
        conversionService.setApiKey(apiKey); // Manually set the API key for the test
    }

    @Test
    void testConvertCurrency() throws Exception {
        double rate = 0.85;
        double amount = 100;
        double convertedAmount = amount * rate;

        CurrencyConversion conversion = CurrencyConversion.builder()
                .id(UUID.randomUUID())
                .convertedAmount(convertedAmount)
                .build();

        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(rate);
        when(repository.save(any(CurrencyConversion.class))).thenReturn(conversion);

        CurrencyConversion result = conversionService.convertCurrency("USD", "EUR", amount);

        assertEquals(convertedAmount, result.getConvertedAmount());
    }

    @Test
    void testGetExchangeRateInvalidCurrency() throws Exception {
        when(exchangeRateService.getExchangeRate(anyString(), anyString()))
                .thenThrow(new CurrencyNotFoundException(ErrorCode.CURRENCY_NOT_FOUND));

        assertThrows(CurrencyNotFoundException.class, () -> {
            conversionService.convertCurrency("USD", "INVALID", 100);
        });
    }
}