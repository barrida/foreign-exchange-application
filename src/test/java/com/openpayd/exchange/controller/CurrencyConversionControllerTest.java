package com.openpayd.exchange.controller;

import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.service.CurrencyConversionService;
import com.openpayd.exchange.service.ExchangeRateService;
import constants.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author suleyman.yildirim
 */
@WebMvcTest(CurrencyConversionController.class)
class CurrencyConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyConversionService conversionService;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    void testConvertCurrency() throws Exception {
        CurrencyConversion conversion = CurrencyConversion.builder()
                .convertedAmount(BigDecimal.valueOf(349.99))
                .build();

        when(conversionService.convertCurrency(TestConstants.USD, TestConstants.TRY, BigDecimal.valueOf(10.55))).thenReturn(conversion);

        mockMvc.perform(post("/v1/convert-currency")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.TRY)
                        .param("amount", "10.55"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.convertedAmount").value(conversion.getConvertedAmount()));
    }

    @Test
    void testConvertCurrency_Invalid_Amount() throws Exception {
        mockMvc.perform(post("/v1/convert-currency")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.TRY)
                        .param("amount", "-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Validation failed: convertCurrency.amount: must be greater than 0.0")))
                .andExpect(jsonPath("$.errorCode").value((ErrorCode.VALIDATION_ERROR.getCode())));
    }

    @Test
    void testConvertCurrency_Empty_Amount() throws Exception {
        mockMvc.perform(post("/v1/convert-currency")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.TRY)
                        .param("amount", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Validation failed: Required request parameter 'amount' for method parameter type BigDecimal is present but converted to null")))
                .andExpect(jsonPath("$.errorCode").value((ErrorCode.VALIDATION_ERROR.getCode())));
    }

    @Test
    void testConvertCurrency_Invalid_Source_And_Target_Currency_Empty() throws Exception {
        mockMvc.perform(post("/v1/convert-currency")
                        .param("sourceCurrency", TestConstants.EMPTY_CURRENCY)
                        .param("targetCurrency", TestConstants.EMPTY_CURRENCY)
                        .param("amount", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("convertCurrency.targetCurrency: must match \"^[A-Z]{3}$\"")))
                .andExpect(jsonPath("$.message").value(containsString("convertCurrency.targetCurrency: must not be blank")))
                .andExpect(jsonPath("$.message").value(containsString("convertCurrency.sourceCurrency: must match \"^[A-Z]{3}$\"")))
                .andExpect(jsonPath("$.message").value(containsString("convertCurrency.sourceCurrency: must not be blank")))
                .andExpect(jsonPath("$.errorCode").value((ErrorCode.VALIDATION_ERROR.getCode())));

    }


    @Test
    void testConvertCurrency_Invalid_Currency_Pattern() throws Exception {
        mockMvc.perform(post("/v1/convert-currency")
                        .param("sourceCurrency", TestConstants.USD)
                        .param("targetCurrency", TestConstants.INVALID_CURRENCY)
                        .param("amount", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(("Validation failed: convertCurrency.targetCurrency: must match \"^[A-Z]{3}$\"")))
                .andExpect(jsonPath("$.errorCode").value((ErrorCode.VALIDATION_ERROR.getCode())));

    }

}
