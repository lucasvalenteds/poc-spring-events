package com.example.events;

public final class CustomerCreated {

    private final Long customerId;

    public CustomerCreated(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
