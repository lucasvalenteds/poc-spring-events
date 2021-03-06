package com.example.customer;

import java.io.Serial;

public final class CustomerNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3708948683389460482L;

    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with ID " + customerId);
    }
}
