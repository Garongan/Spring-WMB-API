package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.request.NewAccountRequest;
import com.enigma.wmb_api_next.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api_next.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api_next.dto.response.CustomerResponse;
import com.enigma.wmb_api_next.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerResponse saveOrGet(CustomerRequest request);
    Customer saveAccount(NewAccountRequest request);
    List<CustomerResponse> saveBulk(List<CustomerRequest> requests);
    CustomerResponse getById(String id);
    List<CustomerResponse> getAll(SearchCustomerRequest request);
    CustomerResponse update(UpdateCustomerRequest request);
    void delete(String id);
}
