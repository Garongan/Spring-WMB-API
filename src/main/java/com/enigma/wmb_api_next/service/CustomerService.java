package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.Customer;

public interface CustomerService {
    Customer getById(String id);

    Customer getAll();

    Customer update(Customer customer);

    void delete(String id);
}
