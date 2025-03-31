package com.org.Rewards;

import com.org.Rewards.entity.Customer;
import com.org.Rewards.entity.Transaction;
import com.org.Rewards.repositories.CustomerRepository;
import com.org.Rewards.repositories.TransactionRepository;
import com.org.Rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Test class for the RewardService.
 */
@ExtendWith(MockitoExtension.class)

public class RewardsServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardsService;

    private Customer testCustomer;
    private List<Transaction> testTransactions;

    /**
     * Sets up the test environment before each test method is executed.
     */
    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustomerId(123L);
        testCustomer.setName("Test Customer");

        testTransactions = new ArrayList<>();
        // Create transactions for testing
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(120);
        transaction1.setDate(LocalDate.of(2024, 10, 15));
        transaction1.setCustomer(testCustomer);
        testTransactions.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(80);
        transaction2.setDate(LocalDate.of(2024, 10, 20));
        transaction2.setCustomer(testCustomer);
        testTransactions.add(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setId(3L);
        transaction3.setAmount(150);
        transaction3.setDate(LocalDate.of(2024, 11, 5));
        transaction3.setCustomer(testCustomer);
        testTransactions.add(transaction3);
        testCustomer.setTransactions(testTransactions);
    }

    /**
     * Tests the successful calculation of rewards for a customer.
     */
    @Test
    @DisplayName("Calculate rewards - successful")
    void calculateRewards_success() {
        when(customerRepository.findById(123L)).thenReturn(Optional.of(testCustomer));

        Map<String, Object> result = rewardsService.calculateRewards(123L, 2);

        Map<YearMonth, Integer> expectedMonthlyRewards = new HashMap<>();
        expectedMonthlyRewards.put(YearMonth.of(2024, 10), 120); // 70 + 20
        expectedMonthlyRewards.put(YearMonth.of(2024, 11), 150);

        assertEquals(expectedMonthlyRewards, result.get("monthlyRewards"));
        assertEquals(270, result.get("totalRewards"));
    }

    @Test
    @DisplayName("Calculate rewards - customer not found")
    void calculateRewards_customerNotFound() {
        when(customerRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rewardsService.calculateRewards(123L, 2));
    }

    @Test
    @DisplayName("Calculate monthly rewards")
    void calculateMonthlyRewards_success() {
        Map<YearMonth, Integer> result = rewardsService.calculateMonthlyRewards(testTransactions);

        Map<YearMonth, Integer> expectedMonthlyRewards = new HashMap<>();
        //   expectedMonthlyRewards.put(YearMonth.of(2024, 10), 90);
        //  expectedMonthlyRewards.put(YearMonth.of(2024, 11), 150);
        expectedMonthlyRewards.put(YearMonth.of(2024, 11), 150); // 70 + 20
        expectedMonthlyRewards.put(YearMonth.of(2024, 10), 120);
        assertEquals(expectedMonthlyRewards, result);
    }

    @Test
    @DisplayName("Calculate total rewards")
    void calculateTotalRewards_success() {
        int result = rewardsService.calculateTotalRewards(testTransactions);
        assertEquals(270, result);
    }
}
