package com.example.customer;

import com.example.events.CustomerCreated;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerService(CustomerRepository customerRepository, ApplicationEventPublisher eventPublisher) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Customer createCustomer(String name) {
        final var customer = new Customer();
        customer.setName(name);
        customer.setActive(false);

        final var customerCreated = customerRepository.save(customer);
        eventPublisher.publishEvent(new CustomerCreated(customerCreated.getId()));

        return customerCreated;
    }

    @Transactional(readOnly = true)
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Transactional
    public void activate(Long customerId) {
        final var customer = this.findById(customerId);
        customer.setActive(true);

        customerRepository.save(customer);
    }
}
