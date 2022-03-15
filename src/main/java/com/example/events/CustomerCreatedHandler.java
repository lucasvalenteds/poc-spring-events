package com.example.events;

import com.example.account.AccountService;
import com.example.customer.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerCreatedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCreatedHandler.class);

    private final AccountService accountService;
    private final CustomerService customerService;

    public CustomerCreatedHandler(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void on(CustomerCreated event) {
        if (event.customerId() == 2L) {
            throw new IllegalArgumentException("Only one customer should be created in this POC");
        }

        final var customer = customerService.findById(event.customerId());
        final var customerId = customer.getId();

        final var accountId = accountService.create(customer);
        LOGGER.info("Account {} created for customer {}", accountId, customerId);

        customerService.activate(customerId);
        LOGGER.info("Customer {} activated", customerId);
    }
}
