package com.org.Rewards.repositories;

import com.org.Rewards.entity.Customer;
import com.org.Rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Transaction entities.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Finds all transactions associated with a given customer ID.
     *
     * @param customerId The ID of the customer.
     * @return A list of Transaction entities associated with the customer ID.
     */
    List<Transaction> findByCustomer_CustomerId(Long customerId);

    /**
     * Finds transactions associated with a given customer.
     *
     * @param testCustomer The Customer entity.
     * @return An object containing the found transactions. (Note: Consider using List<Transaction>).
     */
    Object findByCustomer(Customer testCustomer);
}
