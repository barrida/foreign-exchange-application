package com.openpayd.exchange.service;

import com.openpayd.exchange.exception.CurrencyNotFoundException;
import com.openpayd.exchange.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author suleyman.yildirim
 */

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Value("${currencyapi.api.key}")
    private String apiKey;

    @BeforeEach
    public void setUp() {
        exchangeRateService = new ExchangeRateService();
        exchangeRateService.setApiKey(apiKey);
    }

    @Test
    void testGetExchangeRate() throws Exception {
        String jsonResponse = "{\"data\":{\"EUR\":{\"value\":0.85}}}";
        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getInputStream()).thenReturn(inputStream);

        ExchangeRateService exchangeRateServiceSpy = Mockito.spy(exchangeRateService);
        doReturn(mockConnection).when(exchangeRateServiceSpy).createConnection(anyString());

        double rateValue = exchangeRateServiceSpy.getExchangeRate("USD", "EUR");

        assertEquals(0.85, rateValue);
    }

    @Test
    void testGetExchangeRateInvalidCurrency() throws Exception {
        String jsonResponse = "{\"data\":{}}"; // Empty data to simulate invalid currency response
        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getInputStream()).thenReturn(inputStream);

        ExchangeRateService exchangeRateServiceSpy = Mockito.spy(exchangeRateService);
        doReturn(mockConnection).when(exchangeRateServiceSpy).createConnection(anyString());

        CurrencyNotFoundException exception = assertThrows(CurrencyNotFoundException.class, () -> {
            exchangeRateServiceSpy.getExchangeRate("USD", "INVALID");
        });

        assertEquals(ErrorCode.CURRENCY_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND.getCode(), exception.getErrorCode());
    }

    @Test
    @Disabled(value = "to be done")
    void testGetExchangeRate_withCache() throws Exception {
        String jsonResponse = "{\"data\":{\"EUR\":{\"value\":0.85}}}";
        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getInputStream()).thenReturn(inputStream);

        ExchangeRateService exchangeRateServiceSpy = Mockito.spy(exchangeRateService);
        doReturn(mockConnection).when(exchangeRateServiceSpy).createConnection(anyString());

        double rateValue1 = exchangeRateServiceSpy.getExchangeRate("USD", "EUR");
        assertEquals(0.85, rateValue1);

        // Call the method again to verify that the cache is used
        double rateValue2 = exchangeRateServiceSpy.getExchangeRate("USD", "EUR");
        assertEquals(0.85, rateValue2);

        // Verify that the HTTP connection was only opened once due to caching
        verify(mockConnection, times(1)).connect();
    }

}