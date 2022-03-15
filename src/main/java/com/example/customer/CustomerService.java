package com.example.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(String name) {
        final var customer = new Customer();
        customer.setName(name);
        customer.setActive(false);

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + customerId));
    }

    @Transactional
    public void activate(Long customerId) {
        final var customer = this.findById(customerId);
        customer.setActive(true);

        customerRepository.save(customer);
    }
}
