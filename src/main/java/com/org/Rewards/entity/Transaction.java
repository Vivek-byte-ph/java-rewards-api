package com.org.Rewards.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private double amount;
    @NotNull(message = "Date cannot be null")
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    @Getter
    @Setter
    private Customer customer;
}
