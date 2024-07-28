package com.openpayd.exchange.controller;

import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.service.CurrencyConversionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void testConvertCurrency() throws Exception {
        CurrencyConversion conversion = CurrencyConversion.builder()
                .convertedAmount(85)
                .build();

        Mockito.when(conversionService.convertCurrency(anyString(), anyString(), anyDouble())).thenReturn(conversion);

        mockMvc.perform(post("/v1/convert-currency")
                        .param("sourceCurrency", "USD")
                        .param("targetCurrency", "EUR")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.convertedAmount").value(85.0));
    }
}
