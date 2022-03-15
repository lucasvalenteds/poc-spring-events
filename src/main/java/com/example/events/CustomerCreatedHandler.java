package com.example.events;

import com.example.account.AccountService;
import com.example.customer.CustomerService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerCreatedHandler {

    private final AccountService accountService;
    private final CustomerService customerService;

    public CustomerCreatedHandler(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void on(CustomerCreated event) {
        if (event.getCustomerId() == 2L) {
            throw new IllegalArgumentException("Only one customer should be created in this POC");
        }

        final var customer = customerService.findById(event.getCustomerId());

        accountService.createCustomerAccount(customer);

        customerService.activate(customer.getId());
    }
}
