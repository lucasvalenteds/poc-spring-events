package com.example;

import com.example.account.AccountNotFoundException;
import com.example.account.AccountService;
import com.example.customer.CustomerNotFoundException;
import com.example.customer.CustomerService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Test
    @Order(1)
    void creatingCustomerAndHandlingEvent() {
        assertThrows(AccountNotFoundException.class, () -> accountService.findById(1L));
        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(1L));

        final var customer = customerService.create("John Smith");
        assertEquals(1L, customer.getId());
        assertEquals("John Smith", customer.getName());
        assertFalse(customer.isActive(), "New customers should be inactive by default");

        final var customerAfterEventHandled = customerService.findById(customer.getId());
        assertTrue(customerAfterEventHandled.isActive(), "Customer should be active after CustomerCreated event");

        assertDoesNotThrow(() -> accountService.findById(1L));
        assertDoesNotThrow(() -> customerService.findById(1L));
    }

    @Test
    @Order(2)
    void rollbackCustomerCreationDueToHandlerError() {
        assertThrows(AccountNotFoundException.class, () -> accountService.findById(2L));
        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(2L));

        final var exception = assertThrows(
            IllegalArgumentException.class,
            () -> customerService.create("Mary Jane")
        );

        assertEquals("Only one customer should be created in this POC", exception.getMessage());

        assertThrows(AccountNotFoundException.class, () -> accountService.findById(2L));
        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(2L));
    }
}
