package com.openpayd.exchange.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

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
    private UUID id;

    @PositiveOrZero(message = "convertedAmount must be zero or positive")
    private double convertedAmount;

    private LocalDate createdAt;
}

