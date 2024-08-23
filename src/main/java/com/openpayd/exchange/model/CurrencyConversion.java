package com.openpayd.exchange.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author suleyman.yildirim
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency_conversion", indexes = {
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_source_currency", columnList = "sourceCurrency"),
        @Index(name = "idx_target_currency", columnList = "targetCurrency"),
        @Index(name = "idx_transaction_id_created_at", columnList = "id, createdAt")
})
public class CurrencyConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(name = "A transaction identifier", example = "8426d6fa-53c6-4fad-8744-c07c14bb6092")
    private UUID id;

    @PositiveOrZero(message = "convertedAmount must be zero or positive")
    @Schema(name = "The converted amount in the target currency", example = "32.394954")
    private BigDecimal convertedAmount;

    @Schema(name = "The transaction date", example = "2024-07-29")
    private LocalDate createdAt;

    @Schema(name = "Source currency code", example = "USD")
    private String sourceCurrency;

    @Schema(name = "Target currency code", example = "EUR")
    private String targetCurrency;

    @Schema(name = "Exchange rate used for conversion", example = "0.84")
    private BigDecimal exchangeRate;

}

