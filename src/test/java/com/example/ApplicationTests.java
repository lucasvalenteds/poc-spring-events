package com.example;

import com.example.account.AccountNotFoundException;
import com.example.account.AccountService;
import com.example.customer.CustomerNotFoundException;
import com.example.customer.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Test
    void creatingCustomerAndHandlingEvent() {
        assertThrows(AccountNotFoundException.class, () -> accountService.findById(1L));
        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(1L));

        final var customer = customerService.createCustomer("John Smith");
        assertEquals(1L, customer.getId());
        assertEquals("John Smith", customer.getName());
        assertFalse(customer.isActive(), "New customers should be inactive by default");

        final var customerAfterEventHandled = customerService.findById(customer.getId());
        assertTrue(customerAfterEventHandled.isActive(), "Customer should be active after CustomerCreated event");

        assertDoesNotThrow(() -> accountService.findById(1L));
        assertDoesNotThrow(() -> customerService.findById(1L));
    }
}
