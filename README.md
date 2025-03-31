# Java 8 Rewards Calculation API

This project implements a RESTful API to calculate customer rewards based on transaction amounts. It adheres to Java 8 coding standards and best practices.

## 1. Introduction

This API provides functionalities to:

* Calculate customer rewards based on transaction amounts.
* Allow users to specify a custom time frame for reward calculation.
* Add new transactions for customers.

## 2. Functionalities

* **Rewards Calculation Endpoint:**
    * Calculates rewards for a given customer.
    * The default time frame for reward calculation is 3 months.
    * The API is scalable and dynamic, allowing for future expansion.
* **Dynamic Time Frame:**
    * A parameter is added to the endpoint to allow users to specify a custom time frame for reward calculation.
* **Detailed Response:**
    * The API response includes important information about the customer and transactions, providing a clear picture of the rewards calculation.
    * The response includes customer name, transaction amounts, transaction dates, and total rewards.

## 3. API Endpoints

* **Calculate Rewards:**
    * `GET /rewards/v1/{customerId}?months={months}`
    * Calculates rewards for a given customer within a specified time frame.
    * **Example Request:**
        * `GET /rewards/v1/1?months=3`
    * **Example Response (200 OK):**
        ```json
        {
          "customerName": "User1",
          "totalRewards": 270,
          "transactions": [
            {
              "amount": 120,
              "date": "2024-10-15",
              "rewards": 90
            },
            {
              "amount": 80,
              "date": "2024-10-20",
              "rewards": 30
            },
            {
              "amount": 150,
              "date": "2024-11-05",
              "rewards": 120
            }
          ]
        }
        ```
    * **Example Response (404 Not Found):**
        ```json
        {
          "timestamp": "2025-03-31T14:13:22.528998",
          "status": 404,
          "error": "Not Found",
          "message": "Customer not found",
          "path": "/rewards/v1/999?months=3"
        }
        ```

* **Add Transaction:**
    * `POST /rewards/v1/{customerId}/transactions`
    * Adds a transaction for a given customer.
    * **Example Request:**
        ```json
        {
          "amount": 100.00,
          "date": "2024-10-27"
        }
        ```
    * **Example Response (200 OK):**
        ```
        Transaction added successfully
        ```
    * **Example Response (400 Bad Request):**
        ```json
        {
          "timestamp": "2025-03-31T14:13:22.528998",
          "status": 400,
          "error": "Bad Request",
          "message": "Transaction amount must be greater than zero.",
          "path": "/rewards/v1/1/transactions"
        }
        ```

## 4. Running the Application

1.  **Clone the repository:** `git clone <repository_url>`
2.  **Navigate to the project directory:** `cd <project_directory>`
3.  **Build the application:** `./mvnw clean install`
4.  **Run the application:** `./mvnw spring-boot:run`
5.  **Add database entries in H2 Console:**
    ```sql
    INSERT INTO customer (name) VALUES ('User1');
    INSERT INTO customer (name) VALUES ('User2');
    INSERT INTO transaction (amount, date, customer_id) VALUES (120, '2024-10-15', 1);
    INSERT INTO transaction (amount, date, customer_id) VALUES (80, '2024-10-20', 1);
    INSERT INTO transaction (amount, date, customer_id) VALUES (150, '2024-11-05', 1);
    INSERT INTO transaction (amount, date, customer_id) VALUES (60, '2024-11-12', 2);
    INSERT INTO transaction (amount, date, customer_id) VALUES (200, '2024-12-01', 2);
    INSERT INTO transaction (amount, date, customer_id) VALUES (90, '2024-12-25', 2);
    ```

## 5. Running Tests

1.  **Navigate to the project directory:** `cd <project_directory>`
2.  **Run the tests:** `./mvnw test`

## 6. Dependencies

* Spring Boot
* JUnit 5
* H2 Database
* Lombok
* Slf4j
* Jackson

## 7. Testing

* **Test Cases and Results:**
    * JUnit 5 test cases are implemented to cover various scenarios.
    * Test results are included in the CI/CD pipeline (if applicable) or can be viewed by running the tests locally.
* **Asynchronous API Simulation:**
    * While the API is inherently synchronous, test cases can simulate asynchronous behavior using techniques like `MockMvc` and `CompletableFuture` (if needed for external API simulations).




## Contributing

Please feel free to contribute by submitting pull requests.

## License

[Add License Information]
