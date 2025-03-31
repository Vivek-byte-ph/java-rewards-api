package com.org.Rewards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Rewards.entity.Customer;
import com.org.Rewards.entity.Transaction;
import com.org.Rewards.repositories.CustomerRepository;
import com.org.Rewards.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private Customer testCustomer;


	@BeforeEach
	public void setup() {
		// Clear data before each test
		transactionRepository.deleteAll();
		customerRepository.deleteAll();

		// Create a test customer and transactions
		testCustomer = new Customer();
		testCustomer.setName("Test Customer");
		customerRepository.save(testCustomer);

		Transaction transaction1 = new Transaction();
		transaction1.setAmount(120);
		transaction1.setDate(LocalDate.of(2024, 10, 15));
		transaction1.setCustomer(testCustomer);
		transactionRepository.save(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setAmount(80);
		transaction2.setDate(LocalDate.of(2024, 10, 20));
		transaction2.setCustomer(testCustomer);
		transactionRepository.save(transaction2);

		Transaction transaction3 = new Transaction();
		transaction3.setAmount(150);
		transaction3.setDate(LocalDate.of(2024, 11, 5));
		transaction3.setCustomer(testCustomer);
		transactionRepository.save(transaction3);
	}

	@Test
	public void testGetRewards_DefaultMonths() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/" + testCustomer.getCustomerId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalRewards").value(270));
	}

	@Test
	public void testGetRewardsCustomMonths() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/" + testCustomer.getCustomerId() + "?months=2")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalRewards").value(270));
	}

	@Test
	public void testGetRewards_CustomerNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/999")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Customer not found with id: 999"));
	}

	@Test
	public void testAddTransaction() throws Exception {
		Transaction newTransaction = new Transaction();
		newTransaction.setAmount(200);
		newTransaction.setDate(LocalDate.of(2023, 12, 1));

		mockMvc.perform(MockMvcRequestBuilders.post("/rewards/v1/" + testCustomer.getCustomerId() + "/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newTransaction)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/" + testCustomer.getCustomerId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalRewards").value(520));
	}

	@Test
	public void testAddTransaction_InvalidData() throws Exception {
		Transaction invalidTransaction = new Transaction();
		invalidTransaction.setAmount(-10);

		mockMvc.perform(MockMvcRequestBuilders.post("/rewards/v1/" + testCustomer.getCustomerId() + "/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidTransaction)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void testGetRewards_WithCustomerAndTransactions() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/rewards/v1/" + testCustomer.getCustomerId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Test Customer"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.transactions").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.transactions.length()").value(3));
	}

}