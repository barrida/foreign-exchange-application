package com.openpayd.exchange.repository;

import com.openpayd.exchange.model.CurrencyConversion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author suleyman.yildirim
 */
@Repository
public interface CurrencyConversionRepository extends CrudRepository<CurrencyConversion, UUID> {
}
