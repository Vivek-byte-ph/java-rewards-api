package com.org.Rewards.repositories;

import com.org.Rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomer_CustomerId(Long customerId);
}
