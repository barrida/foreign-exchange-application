package com.openpayd.exchange.config;

import com.openpayd.exchange.model.CurrencyConversion;
import com.openpayd.exchange.repository.CurrencyConversionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

/**
 * @author suleyman.yildirim
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final Random rand = SecureRandom.getInstanceStrong();
    private final CurrencyConversionRepository repository;

    public DataLoader(CurrencyConversionRepository repository) throws NoSuchAlgorithmException {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        var rValue = this.rand.nextInt();
        for (int i = 0; i < 100000; i++) {
            CurrencyConversion conversion = CurrencyConversion.builder()
                    .id(UUID.randomUUID())
                    .convertedAmount(BigDecimal.valueOf(Math.random() * 100))
                    .createdAt(LocalDate.now().minusDays((rValue * 365)))
                    .sourceCurrency("USD")
                    .targetCurrency("EUR")
                    .exchangeRate(BigDecimal.valueOf(0.83))
                    .build();

            repository.save(conversion);
        }
    }
}

