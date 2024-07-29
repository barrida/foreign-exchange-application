package com.openpayd.exchange.service;

import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.repository.CurrencyConversionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ConversionHistoryServiceTest {

    @Mock
    private CurrencyConversionRepository repository;

    @InjectMocks
    private ConversionHistoryService historyService;

    @BeforeEach
    public void setUp() {
        historyService = new ConversionHistoryService(repository);
    }

    @Test
    void testGetConversionHistoryByTransactionId() {
        UUID transactionId = UUID.randomUUID();
        CurrencyConversion conversion = CurrencyConversion.builder()
                .id(transactionId)
                .convertedAmount(85)
                .createdAt(LocalDate.now())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<CurrencyConversion> page = new PageImpl<>(Collections.singletonList(conversion));

        when(repository.findById(transactionId, pageable)).thenReturn(page);

        Page<CurrencyConversion> result = historyService.getConversionHistoryByTransactionId(transactionId,  pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(transactionId, result.getContent().get(0).getId());
    }

    @Test
    void testGetConversionHistoryByDate() {
        LocalDate transactionDate = LocalDate.now().minusDays(1);
        CurrencyConversion conversion = CurrencyConversion.builder()
                .id(UUID.randomUUID())
                .convertedAmount(85)
                .createdAt(LocalDate.now())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<CurrencyConversion> page = new PageImpl<>(Collections.singletonList(conversion));

        when(repository.findByCreatedAt(transactionDate, pageable)).thenReturn(page);

        Page<CurrencyConversion> result = historyService.getConversionHistoryByDate(transactionDate, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetConversionHistoryByIdAndCreatedAt() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        LocalDate transactionDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CurrencyConversion> expectedPage = new PageImpl<>(List.of(new CurrencyConversion()));

        when(repository.findByIdAndCreatedAt(any(UUID.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<CurrencyConversion> actualPage = historyService.getConversionHistoryByIdAndCreatedAt(transactionId, transactionDate, pageable);

        // Assert
        assertEquals(expectedPage, actualPage);
    }
}
