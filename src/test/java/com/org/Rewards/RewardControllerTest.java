package com.org.Rewards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Rewards.controller.RewardController;
import com.org.Rewards.entity.Customer;
import com.org.Rewards.entity.Transaction;
import com.org.Rewards.exception.CustomerNotFoundException;
import com.org.Rewards.repositories.CustomerRepository;
import com.org.Rewards.repositories.TransactionRepository;
import com.org.Rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Test Customer");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);
        transaction.setDate(LocalDate.now());
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);
    }

    @Test
    public void getRewards_ValidCustomerId_ReturnsOkWithRewards() throws Exception {
        Map<String, Object> rewards = new HashMap<>();
        rewards.put("totalRewards", 270);
        when(rewardService.calculateRewards(anyLong(), anyInt())).thenReturn(rewards);

        mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalRewards").value(270))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Test Customer"));
    }

    @Test
    public void getRewards_CustomerNotFound_ReturnsNotFound() throws Exception {
        when(rewardService.calculateRewards(anyLong(), anyInt())).thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/999"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Customer not found"));
    }

    @Test
    public void getRewards_NullCustomerId_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/null"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void addTransaction_ValidData_ReturnsOk() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);
        transaction.setDate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/rewards/v1/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk());
    }

    @Test
    public void addTransaction_InvalidAmount_ReturnsBadRequest() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAmount(-10.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/rewards/v1/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTransaction_CustomerNotFound_ReturnsNotFound() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/rewards/v1/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }
}