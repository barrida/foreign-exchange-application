package com.openpayd.exchange.controller;

import com.openpayd.exchange.exception.CurrencyNotFoundException;
import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.service.ExchangeRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
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
        Mockito.when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(0.85);

        var result = mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", "USD")
                        .param("targetCurrency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals("0.85", result.getResponse().getContentAsString());
    }

    @Test
    void testGetExchangeRateInvalidCurrency() throws Exception {
        Mockito.when(exchangeRateService.getExchangeRate(anyString(), anyString()))
                .thenThrow(new CurrencyNotFoundException(ErrorCode.CURRENCY_NOT_FOUND));

        mockMvc.perform(get("/v1/exchange-rate")
                        .param("sourceCurrency", "USD")
                        .param("targetCurrency", "INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(ErrorCode.CURRENCY_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.CURRENCY_NOT_FOUND.getCode()));
    }

}
