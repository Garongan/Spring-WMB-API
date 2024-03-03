package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.response.CustomerResponse;
import com.enigma.wmb_api_next.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer saveOrGet(String name);
    CustomerResponse save(CustomerRequest request);
    List<CustomerResponse> saveBulk(List<CustomerRequest> requests);
    CustomerResponse getById(String id);

    List<CustomerResponse> getAll();

    CustomerResponse update(Customer customer);

    void delete(String id);
}
