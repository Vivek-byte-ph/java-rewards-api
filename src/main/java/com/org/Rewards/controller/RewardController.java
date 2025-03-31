package com.org.Rewards.controller;

import com.org.Rewards.entity.Customer;
import com.org.Rewards.entity.Transaction;
import com.org.Rewards.exception.CustomerNotFoundException;
import com.org.Rewards.exception.InvalidTransactionException;
import com.org.Rewards.repositories.CustomerRepository;
import com.org.Rewards.repositories.TransactionRepository;
import com.org.Rewards.service.RewardService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for handling customer rewards and transactions.
 */
@RestController
@RequestMapping("/rewards/v1")
public class RewardController {
    private static final Logger logger = LoggerFactory.getLogger(RewardController.class);

    @Autowired
    private RewardService rewardService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Retrieves rewards information for a customer.
     *
     * @param customerId The ID of the customer.
     * @param months     The number of months to calculate rewards for (default is 3).
     * @return ResponseEntity containing rewards information or an error message.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<Map<String, Object>> getRewards(@PathVariable Long customerId, @RequestParam(defaultValue = "3") int months) {
        if (customerId == null) {
            logger.error("CustomerId is null.");
            throw new CustomerNotFoundException("Customer ID cannot be null.");
        }
        try {
            logger.info("getRewards called for customerId: {}", customerId);
            Map<String, Object> rewards = rewardService.calculateRewards(customerId, months);
            Customer customer = customerRepository.findById(customerId).orElse(null);
            if (customer == null) {
                logger.warn("Customer not found for customerId: {}", customerId);
            } else {
                logger.info("Customer found: {}", customer.getName());
            }
            List<Transaction> transactions = transactionRepository.findByCustomer_CustomerId(customerId);
            Map<String, Object> response = new HashMap<>(rewards);
            if (customer != null) {
                response.put("customerName", customer.getName());
            }
            if (transactions != null && !transactions.isEmpty()) {
                response.put("transactions", transactions.stream()
                        .map(transaction -> Map.of(
                                "transactionId", transaction.getId(),
                                "amount", transaction.getAmount(),
                                "date", transaction.getDate()
                        ))
                        .collect(Collectors.toList()));
            }
            return ResponseEntity.ok(response);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Adds a transaction for a customer.
     *
     * @param customerId  The ID of the customer.
     * @param transaction The transaction to add.
     * @return ResponseEntity indicating success or an error message.
     */
    @PostMapping("/{customerId}/transactions")
    public ResponseEntity<String> addTransaction(@PathVariable Long customerId, @Valid @RequestBody Transaction transaction) {
        try {
            if (transaction.getAmount() <= 0) {
                throw new InvalidTransactionException("Transaction amount must be greater than zero.");
            }
            Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
            transaction.setCustomer(customer);
            transactionRepository.save(transaction);
            return ResponseEntity.ok("Transaction added successfully");
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidTransactionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


