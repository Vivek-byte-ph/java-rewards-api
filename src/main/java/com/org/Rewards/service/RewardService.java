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

@Service
public class RewardService {
    @Autowired
    private CustomerRepository customerRepository;
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
    public Map<YearMonth, Integer> calculateMonthlyRewards(List<Transaction> transactions) {
        Map<YearMonth, Integer> monthlyRewards = new HashMap<>();
        for (Transaction transaction : transactions) {
            int points = calculatePoints(transaction.getAmount());
            YearMonth month = YearMonth.from(transaction.getDate());
            monthlyRewards.merge(month, points, Integer::sum);
        }
        return monthlyRewards;
    }
    public int calculateTotalRewards(List<Transaction> transactions) {
        return transactions.stream().mapToInt(transaction -> calculatePoints(transaction.getAmount())).sum();
    }
    private int calculatePoints(double amount) {
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
