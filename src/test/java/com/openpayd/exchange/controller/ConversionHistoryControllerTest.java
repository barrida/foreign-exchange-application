package com.openpayd.exchange.controller;


import com.openpayd.exchange.exception.ErrorCode;
import com.openpayd.exchange.exception.InvalidInputException;
import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.service.ConversionHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author suleyman.yildirim
 */
@WebMvcTest(ConversionHistoryController.class)
class ConversionHistoryControllerTest {

    private static final double CONVERTED_AMOUNT = 85.0;

    private static final LocalDate TRANSACTION_DATE = LocalDate.of(2024, 5, 5);

    private static final UUID TRANSACTION_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversionHistoryService historyService;

    @Test
    void testGetConversionHistoryByTransactionId() throws Exception {
        CurrencyConversion conversion = CurrencyConversion.builder()
                .id(TRANSACTION_ID)
                .convertedAmount(CONVERTED_AMOUNT)
                .createdAt(TRANSACTION_DATE)
                .build();

        Page<CurrencyConversion> page = new PageImpl<>(Collections.singletonList(conversion), PageRequest.of(0, 10), 1);

        when(historyService.getConversionHistoryByTransactionId(TRANSACTION_ID, PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/v1/conversion-history")
                        .param("transactionId", TRANSACTION_ID.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(TRANSACTION_ID.toString()))
                .andExpect(jsonPath("$.content[0].convertedAmount").value(CONVERTED_AMOUNT))
                .andExpect(jsonPath("$.content[0].createdAt").value(TRANSACTION_DATE.toString()));
    }

    @Test
    void testGetConversionHistoryByDate() throws Exception {
        CurrencyConversion conversion = CurrencyConversion.builder()
                .id(TRANSACTION_ID)
                .convertedAmount(CONVERTED_AMOUNT)
                .createdAt(TRANSACTION_DATE)
                .build();

        Page<CurrencyConversion> page = new PageImpl<>(Collections.singletonList(conversion), PageRequest.of(0, 10), 1);

        when(historyService.getConversionHistoryByDate(TRANSACTION_DATE, PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/v1/conversion-history")
                        .param("transactionDate", TRANSACTION_DATE.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(TRANSACTION_ID.toString()))
                .andExpect(jsonPath("$.content[0].convertedAmount").value(CONVERTED_AMOUNT))
                .andExpect(jsonPath("$.content[0].createdAt").value(TRANSACTION_DATE.toString()));
    }

    @Test
    void testGetConversionHistoryByIdAndCreatedAt() throws Exception {
        CurrencyConversion conversion = CurrencyConversion.builder()
                .id(TRANSACTION_ID)
                .convertedAmount(CONVERTED_AMOUNT)
                .createdAt(TRANSACTION_DATE)
                .build();

        Page<CurrencyConversion> page = new PageImpl<>(Collections.singletonList(conversion), PageRequest.of(0, 10), 1);

        when(historyService.getConversionHistoryByIdAndCreatedAt(any(UUID.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/conversion-history")
                        .param("transactionId", TRANSACTION_ID.toString())
                        .param("transactionDate", TRANSACTION_DATE.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(TRANSACTION_ID.toString()))
                .andExpect(jsonPath("$.content[0].convertedAmount").value(CONVERTED_AMOUNT))
                .andExpect(jsonPath("$.content[0].createdAt").value(TRANSACTION_DATE.toString()));
    }

    @Test
    void testGetConversionHistoryByInvalidDate() throws Exception {
        when(historyService.getConversionHistoryByDate(any(), any()))
                .thenThrow(new InvalidInputException(ErrorCode.INVALID_INPUT));

        mockMvc.perform(get("/v1/conversion-history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.getCode()));
    }

    @Test
    void testGetConversionHistoryByDateNullDates() throws Exception {
        when(historyService.getConversionHistoryByDate(isNull(), any(Pageable.class)))
                .thenThrow(new InvalidInputException(ErrorCode.INVALID_INPUT));

        mockMvc.perform(get("/v1/conversion-history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.getCode()));
    }

    @Test
    void testGetConversionHistoryByNullTransactionId() throws Exception {
        when(historyService.getConversionHistoryByTransactionId(isNull(), any(Pageable.class)))
                .thenThrow(new InvalidInputException(ErrorCode.INVALID_INPUT));

        mockMvc.perform(get("/v1/conversion-history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.getCode()));
    }

    @Test
    void testGetConversionHistoryNoParameters() throws Exception {
        when(historyService.getConversionHistoryByDate(isNull(), any(Pageable.class)))
                .thenThrow(new InvalidInputException(ErrorCode.INVALID_INPUT));

        mockMvc.perform(get("/v1/conversion-history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.getCode()));
    }

}

