# POC: Spring Events

It demonstrates how to implement business logic using an event-based approach.

We want to develop an application to manage customers. It should allows us to create a customer and automatically change a flag which indicated whether the customer is active or not. The application also should create an account which references the customer so we can use that for internal processes instead of the customer representation itself. Data must be stored in a relational database, so the changes must run inside a transaction and any errors should rollback the changes made.

The application is implemented using standard Spring Framework components such as Core and Context, but depends on Spring Boot auto-configuration for convenience. The persistence is implemented using Spring Data JDBC using an annotation approach and the data is stored in an in-memory H2 database provisioned using database migrations managed by Flyway.

No Web layer is implemented. The source code is verified using automated tests run using JUnit and Spring Boot Test.

## Software Design

The source code is structure is pretty standard. Each package is named after a domain concept (e.g: customer, account) and the participants are named based on their responsibilities. The domain class represents the entity, participants named Repository are responsible to persist data and participants named Service contains business logic. For example, we have `Customer` class containing personal data such as person name, `CustomerRepository` persisting that data into the database and `CustomerService` coordinating and exposing customer-related functionality (mostly CRUD operations) to other contexts and participants (e.g: Web layer, other Service classes).

We also have participants to represent events and event Handlers. The event is something that happens in the past and is relevant to the business. The event handler is a participant that does something when the event happens. For example, the event `CustomerCreated` is handled by `CustomerCreatedHandler` and it is responsible to create an account for the customer and activate it. The handler is an asynchronous operation triggered whenever a participant publishes an event of a give type to Spring's message publisher implementation.

Even though the code of a Handler should be seen as asynchronous, it may share the same transaction as a Service, so we can rollback user creation in case the account creation operation fails, for example.

## How to run

| Command | Description |
| :--- | :--- |
| Run tests | `./mvnw test` |
