# Java 8 Rewards Calculation API

This project implements a RESTful API to calculate customer rewards based on transaction amounts. It adheres to Java 8 coding standards and best practices.

* **README.md:**
    * This `README.md` file provides comprehensive documentation for the project.
      ## 6. Functionalities

* **Rewards Calculation Endpoint:**
    * An endpoint is implemented to calculate rewards for a given customer.
    * The default time frame for reward calculation is 3 months.
    * The API is scalable and dynamic, allowing for future expansion.
* **Dynamic Time Frame:**
    * A parameter is added to the endpoint to allow users to specify a custom time frame for reward calculation.
* **Detailed Response:**
    * The API response includes important information about the customer and transactions, providing a clear picture of the rewards calculation.
    * The response includes customer name, transaction amounts, transaction dates, and total rewards.

## API Endpoints

* `GET /rewards/v1/{customerId}?months={months}`: Calculates rewards for a given customer within a specified time frame.
* `POST /rewards/v1/{customerId}/transactions`: adds a transaction for a given customer.

## Running the Application

1.  **Clone the repository:** `git clone <repository_url>`
2.  **Navigate to the project directory:** `cd <project_directory>`
3.  **Build the application:** `./mvnw clean install`
4.  **Run the application:** `./mvnw spring-boot:run`

## Running Tests

1.  **Navigate to the project directory:** `cd <project_directory>`
2.  **Run the tests:** `./mvnw test`

## Dependencies

* Spring Boot
* JUnit 5
* H2 Database
* Lombok
* Slf4j
* Jackson


#.2 Testing

* **Test Cases and Results:**
    * JUnit 5 test cases are implemented to cover various scenarios.
    * Test results are included in the CI/CD pipeline (if applicable) or can be viewed by running the tests locally.
* **Asynchronous API Simulation:**
    * While the API is inherently synchronous, test cases can simulate asynchronous behavior using techniques like `MockMvc` and `CompletableFuture` (if needed for external API simulations).



## 3. Functionalities

* **Rewards Calculation Endpoint:**
    * An endpoint is implemented to calculate rewards for a given customer.
    * The default time frame for reward calculation is 3 months.
    * The API is scalable and dynamic, allowing for future expansion.
* **Dynamic Time Frame:**
    * A parameter is added to the endpoint to allow users to specify a custom time frame for reward calculation.
* **Detailed Response:**
    * The API response includes important information about the customer and transactions, providing a clear picture of the rewards calculation.
    * The response includes customer name, transaction amounts, transaction dates, and total rewards.

## API Endpoints

* `GET /rewards/v1/{customerId}?months={months}`: Calculates rewards for a given customer within a specified time frame.
* `POST /rewards/v1/{customerId}/transactions`: adds a transaction for a given customer.
*  Swgger #http://localhost:8080/swagger-ui/index.html#/reward-controller/addTransaction

## Running the Application

1.  **Clone the repository:** `git clone <repository_url>`
2.  **Navigate to the project directory:** `cd <project_directory>`
3.  **Build the application:** `./mvnw clean install`
4.  **Run the application:** `./mvnw spring-boot:run`
5.  **Add db entry in h2console
 NSERT INTO customer (name) VALUES ('User1');
INSERT INTO customer (name) VALUES ('User2');
INSERT INTO transaction (amount, date, customer_id) VALUES (120, '2024-10-15', 1);
INSERT INTO transaction (amount, date, customer_id) VALUES (80, '2024-10-20', 1);
INSERT INTO transaction (amount, date, customer_id) VALUES (150, '2024-11-05', 1);
INSERT INTO transaction (amount, date, customer_id) VALUES (60, '2024-11-12', 2);
INSERT INTO transaction (amount, date, customer_id) VALUES (200, '2024-12-01', 2);
INSERT INTO transaction (amount, date, customer_id) VALUES (90, '2024-12-25', 2);

## Running Tests

1.  **Navigate to the project directory:** `cd <project_directory>`
2.  **Run the tests:** `./mvnw test`

## Dependencies

* Spring Boot
* JUnit 5
* H2 Database
* Lombok
* Slf4j
* Jackson



## Contributing

Please feel free to contribute by submitting pull requests.

## License

[Add License Information]
