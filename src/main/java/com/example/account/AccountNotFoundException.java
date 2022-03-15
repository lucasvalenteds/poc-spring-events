package com.example.account;

import java.io.Serial;

public final class AccountNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6923850797756242444L;

    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID " + accountId);
    }
}
