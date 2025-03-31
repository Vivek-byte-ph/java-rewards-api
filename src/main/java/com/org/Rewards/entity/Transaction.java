package com.org.Rewards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Model class for Transaction
 */
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    @Getter
    @Setter
    private double amount;
    @NotNull(message = "Date cannot be null")
    @Getter
    @Setter
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    @Getter
    @Setter
    private Customer customer;


}
