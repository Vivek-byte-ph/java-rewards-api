package com.org.Rewards.service;

import com.org.Rewards.entity.Customer;
import com.org.Rewards.entity.Transaction;
import com.org.Rewards.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This service class calculates rewards points for customer transactions.
 */
@Service
public class RewardService {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Calculates rewards for a customer based on their transactions.
     *
     * @param customerId The ID of the customer.
     * @param months     The number of months to consider for rewards calculation.
     * @return A map containing the total rewards and monthly rewards breakdown.
     * @throws RuntimeException if the customer is not found.
     */

    public Map<String, Object> calculateRewards(Long customerId, int months) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            Map<String, Object> result = new HashMap<>();
            result.put("monthlyRewards", calculateMonthlyRewards(customer.getTransactions()));
            result.put("totalRewards", calculateTotalRewards(customer.getTransactions()));
            return result;
        } else {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
    }

    /**
     * Calculates the monthly rewards for a list of transactions.
     *
     * @param transactions The list of transactions.
     * @return A map containing the monthly rewards breakdown.
     */
    public Map<YearMonth, Integer> calculateMonthlyRewards(List<Transaction> transactions) {
        Map<YearMonth, Integer> monthlyRewards = new HashMap<>();
        for (Transaction transaction : transactions) {
            int points = calculatePoints(transaction.getAmount());
            YearMonth month = YearMonth.from(transaction.getDate());
            monthlyRewards.merge(month, points, Integer::sum);
        }
        return monthlyRewards;
    }

    /**
     * Calculates the total rewards for a list of transactions.
     *
     * @param transactions The list of transactions.
     * @return The total rewards points.
     */
    public int calculateTotalRewards(List<Transaction> transactions) {
        return transactions.stream().mapToInt(transaction -> calculatePoints(transaction.getAmount())).sum();
    }

    /**
     * Calculates the rewards points for a single transaction amount.
     *
     * @param amount The transaction amount.
     * @return The calculated rewards points.
     */
    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
            amount = 100;
        }
        if (amount > 50) {
            points += (amount - 50);
        }
        return points;
    }


}
