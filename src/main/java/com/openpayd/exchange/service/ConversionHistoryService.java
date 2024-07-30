package com.openpayd.exchange.service;

import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.repository.CurrencyConversionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author suleyman.yildirim
 */
@Service
public class ConversionHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(ConversionHistoryService.class);

    private final CurrencyConversionRepository repository;

    @Autowired
    public ConversionHistoryService(CurrencyConversionRepository repository) {
        this.repository = repository;
    }

    public Page<CurrencyConversion> getConversionHistoryByTransactionId(UUID transactionId, Pageable pageable) {
        logger.info("Fetching conversion history by transaction ID: {}", transactionId);
        Page<CurrencyConversion> conversationHistory = repository.findById(transactionId, pageable);
        logger.info("Fetched {} records for transaction ID: {}", conversationHistory.getTotalElements(), transactionId);
        return conversationHistory;
    }

    public Page<CurrencyConversion> getConversionHistoryByDate(LocalDate transactionDate, Pageable pageable) {
        logger.info("Fetching conversion history by date: {}", transactionDate);
        Page<CurrencyConversion> conversationHistory = repository.findByCreatedAt(transactionDate, pageable);
        logger.info("Fetched {} records for date: {}", conversationHistory.getTotalElements(), transactionDate);
        return conversationHistory;
    }

    public Page<CurrencyConversion> getConversionHistoryByIdAndCreatedAt(UUID transactionId, LocalDate transactionDate, Pageable pageable) {
        logger.info("Fetching conversion history by transaction ID: {} and date: {}", transactionId, transactionDate);
        Page<CurrencyConversion> conversationHistory = repository.findByIdAndCreatedAt(transactionId, transactionDate, pageable);
        logger.info("Fetched {} records for transaction ID: {} and date: {}", conversationHistory.getTotalElements(), transactionId, transactionDate);
        return conversationHistory;
    }

}