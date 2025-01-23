
# Project Overview

This project is a comprehensive testing framework developed using **Java**, **Selenium**, **Maven**, and **Cucumber** following the **Behavior Driven Development (BDD)** approach. It incorporates the **Page Object Model** and the **Singleton Design Pattern** to ensure maintainability and scalability. The framework is designed for testing a reservations application across three layers: UI, Database (PostgreSQL), and API.

## Technologies and Tools Used

- **Java**: The primary programming language used for developing the automation framework.

- **Selenium**: An open-source tool for automating web browsers, enabling UI testing of web applications.

- **Maven**: A build automation tool used for managing project dependencies and building the project.

- **Cucumber**: A BDD framework that allows writing tests in a human-readable format using Gherkin syntax. It integrates seamlessly with Selenium for UI testing.

- **JUnit 4**: A widely used testing framework for Java that provides assertions to validate expected outcomes in tests.

- **Page Object Model (POM)**: A design pattern that enhances test maintenance and reduces code duplication by creating an object repository for web elements.

- **Singleton Design Pattern**: Ensures that a class has only one instance and provides a global point of access to it, particularly useful for managing WebDriver instances.

- **PostgreSQL**: A powerful, open-source relational database used for storing test data and verifying database interactions.

- **RestAssured**: A Java library for testing RESTful APIs, enabling easy verification of API responses.

## Project Structure

The project is organized into several key components:

1. **Feature Files**: Located in the `src/test/resources/features` directory, these files contain scenarios written in Gherkin syntax that describe the expected behavior of the application.

2. **Step Definitions**: Implementations of the steps defined in feature files, located in `src/test/java/stepDefinitions`.

3. **Page Objects**: Classes that encapsulate the web elements and actions for each page of the application, located in `src/test/java/pageObjects`.

4. **Test Runner**: A class responsible for executing Cucumber tests, configured to specify which feature files to run.

5. **Database Tests**: Scripts that validate data integrity and interactions with the PostgreSQL database.

6. **API Tests**: Tests that verify API endpoints using RestAssured, ensuring that they return expected responses.

## Running Tests

To run the tests, navigate to the project directory in your terminal and use Maven commands:

```bash
mvn clean test
```

This command will execute all tests defined in your feature files. You can also run specific features by specifying their paths:

```bash
mvn test -Dcucumber.options="classpath:features/your_feature_file.feature"
```

## Conclusion

This project serves as a robust framework for testing web applications through UI, database, and API layers. By leveraging modern technologies and design patterns, it ensures efficient test execution and maintenance.

For further details or inquiries, you can connect with the author on LinkedIn: [Halim Iltas](https://www.linkedin.com/in/halim-iltas/).