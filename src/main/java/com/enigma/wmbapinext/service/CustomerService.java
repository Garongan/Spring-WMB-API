package com.enigma.wmbapinext.service;

import com.enigma.wmbapinext.entity.Customer;

public interface CustomerService {
    Customer getById(String id);

    Customer getAll();

    Customer update(Customer customer);

    void delete(String id);
}
