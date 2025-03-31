package com.org.Rewards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Model class for Customer
 */
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long customerId;
    @Column(name = "name") // Ensure lowercase "name"
    @Getter
    @Setter
    private String name;
    @Column(name = "\"month\"")
    private String month;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private List<Transaction> transactions;


}
