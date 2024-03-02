package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer save(Customer customer);

    List<Customer> saveBulk(List<Customer> customers);
    Customer getById(String id);

    List<Customer> getAll();

    Customer update(Customer customer);

    void delete(String id);
}
