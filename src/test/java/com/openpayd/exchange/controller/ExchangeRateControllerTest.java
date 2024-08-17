package com.openpayd.exchange.controller;

import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.exception.ExternalApiException;
import com.openpayd.exchange.service.ExchangeRateService;
import constants.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;

import static com.openpayd.exchange.exception.ErrorCode.EXTERNAL_API_ERROR;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author suleyman.yildirim
 */
@WebMvcTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    void testGetExchangeRate() throws Exception {
        Mockito.when(exchangeRateService.getExchangeRate(TestConstants.USD, TestConstants.EUR)).thenReturn(BigDecimal.valueOf(0.85));

        var result = mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.EUR))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals("0.85", result.getResponse().getContentAsString());
    }

    @Test
    @Disabled(value = "To be done")
    void testGetExchangeRate_withCache() throws Exception {

        String cacheKey = "USD-EUR";

        Mockito.when(exchangeRateService.getExchangeRate(TestConstants.USD, TestConstants.EUR)).thenReturn(BigDecimal.valueOf(0.85));

        // First request
        var result1 = mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.EUR))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals("0.85", result1.getResponse().getContentAsString());

        // Second request to test caching
        var result2 = mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.EUR))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals("0.85", result2.getResponse().getContentAsString());

        // Verify that the service method was called only once due to caching
        verify(exchangeRateService, times(1)).getExchangeRate(TestConstants.USD, TestConstants.EUR);
    }

    @Test
    void testGetExchangeRateInvalidCurrency() throws Exception {

        mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.INVALID_CURRENCY))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed: getExchangeRate.targetCurrency: must match \"^[A-Z]{3}$\""))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_ERROR.getCode()));
    }


    @Test
    void testGetExchangeRateWithIOException() throws Exception {

        when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenThrow(new ExternalApiException("Failed to connect to external API"));

        mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.EUR))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed: An error occurred while communicating with the external service: Failed to connect to external API"))
                .andExpect(jsonPath("$.errorCode").value(EXTERNAL_API_ERROR.getCode()));
    }

    @Test
    void testGetExchangeRateWithGenericException() throws Exception {

        Throwable e = new IOException("Connection timed out");
        when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenThrow(new ExternalApiException("Unexpected error occurred while fetching exchange rate", e));

        mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.EUR))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed: An error occurred while communicating with the external service: Unexpected error occurred while fetching exchange rate"))
                .andExpect(jsonPath("$.errorCode").value(EXTERNAL_API_ERROR.getCode()));
    }
}
