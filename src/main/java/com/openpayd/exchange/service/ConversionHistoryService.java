package com.openpayd.exchange.service;

import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.repository.CurrencyConversionRepository;
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
    private final CurrencyConversionRepository repository;

    @Autowired
    public ConversionHistoryService(CurrencyConversionRepository repository) {
        this.repository = repository;
    }

    public Page<CurrencyConversion> getConversionHistoryByTransactionId(UUID transactionId, Pageable pageable) {
        return repository.findById(transactionId, pageable);

    }

    public Page<CurrencyConversion> getConversionHistoryByDate(LocalDate transactionDate, Pageable pageable) {
        return repository.findByCreatedAt(transactionDate, pageable);
    }

    public Page<CurrencyConversion> getConversionHistoryByIdAndCreatedAt(UUID transactionId, LocalDate transactionDate, Pageable pageable){
        return repository.findByIdAndCreatedAt(transactionId, transactionDate, pageable);
    }

}