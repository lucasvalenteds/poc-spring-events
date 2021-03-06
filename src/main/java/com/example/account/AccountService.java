package com.example.account;

import com.example.customer.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Long create(Customer customer) {
        final var account = new Account();
        account.setCustomerId(customer.getId());

        final var accountCreated = accountRepository.save(account);

        return accountCreated.getId();
    }

    @Transactional(readOnly = true)
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
