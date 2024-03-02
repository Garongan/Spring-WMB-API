package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.entity.Customer;
import com.enigma.wmb_api_next.repository.CustomerRepository;
import com.enigma.wmb_api_next.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public List<Customer> saveBulk(List<Customer> customers) {
        return customerRepository.saveAllAndFlush(customers);
    }

    @Override
    public Customer getById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found"));
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer customer) {
        getById(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void delete(String id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
    }
}
