package com.openpayd.exchange.repository;

import com.openpayd.exchange.model.CurrencyConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author suleyman.yildirim
 */
@Repository
public interface CurrencyConversionRepository extends CrudRepository<CurrencyConversion, UUID> {

    Page<CurrencyConversion> findById(UUID id, Pageable pageable);

    Page<CurrencyConversion> findByCreatedAt(LocalDate transactionDate, Pageable pageable);

    Page<CurrencyConversion> findByIdAndCreatedAt(UUID id, LocalDate transactionDate, Pageable pageable);

}
