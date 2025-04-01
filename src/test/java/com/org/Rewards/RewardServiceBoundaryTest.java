package com.org.Rewards;

import com.org.Rewards.entity.Customer;
import com.org.Rewards.repositories.CustomerRepository;
import com.org.Rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardServiceBoundaryTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardService rewardService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustomerId(1L);
    }

    @Test
    void calculateRewards_CustomerIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rewardService.calculateRewards(1L, 3));
    }

    @Test
    void calculateRewards_CustomerHasNoTransactions() {
        testCustomer.setTransactions(Collections.emptyList());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        assertEquals(0, rewardService.calculateRewards(1L, 3).get("totalRewards"));
    }

    @Test
    void calculatePoints_AmountZero() {
        assertEquals(0, rewardService.calculatePoints(0));
    }

    @Test
    void calculatePoints_AmountFifty() {
        assertEquals(0, rewardService.calculatePoints(50));
    }

    @Test
    void calculatePoints_AmountFiftyOne() {
        assertEquals(1, rewardService.calculatePoints(51));
    }

    @Test
    void calculatePoints_AmountHundred() {
        assertEquals(50, rewardService.calculatePoints(100));
    }

    @Test
    void calculatePoints_AmountHundredOne() {
        assertEquals(52, rewardService.calculatePoints(101));
    }

    @Test
    void calculatePoints_AmountNegative() {
        assertEquals(0, rewardService.calculatePoints(-10));
    }

    @Test
    void calculatePoints_MaxDouble() {
        assertEquals(Integer.MAX_VALUE, rewardService.calculatePoints(Double.MAX_VALUE));
    }

    @Test
    void calculateMonthlyRewards_EmptyTransactionList() {
        assertEquals(0, rewardService.calculateMonthlyRewards(new ArrayList<>()).size());
    }
}
