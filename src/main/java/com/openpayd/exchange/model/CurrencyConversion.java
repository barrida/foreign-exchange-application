package com.openpayd.exchange.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}

