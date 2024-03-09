package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.request.NewAccountRequest;
import com.enigma.wmb_api_next.dto.response.CustomerResponse;
import com.enigma.wmb_api_next.entity.Customer;
import com.enigma.wmb_api_next.repository.CustomerRepository;
import com.enigma.wmb_api_next.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse saveOrGet(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhoneNumber())
                .build();
        Customer saved = customerRepository.findByNameLikeIgnoreCaseAndPhoneEquals("%" + request.getName() + "%", request.getPhoneNumber()).orElseGet(() -> customerRepository.saveAndFlush(customer));
        return CustomerResponse.builder()
                .name(saved.getName())
                .phoneNumber(saved.getPhone())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAccount(NewAccountRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .userAccount(request.getUserAccount())
                .build();
        customerRepository.saveAndFlush(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CustomerResponse> saveBulk(List<CustomerRequest> requests) {
        List<CustomerResponse> responses = new ArrayList<>();
        List<Customer> customers = requests.stream().map(
                request -> {
                    Customer build = Customer.builder()
                            .name(request.getName())
                            .phone(request.getPhoneNumber())
                            .build();
                    responses.add(CustomerResponse.builder().name(request.getName()).phoneNumber(request.getPhoneNumber()).build());
                    return build;
                }
        ).toList();

        customerRepository.saveAllAndFlush(customers);

        return responses;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse getById(String id) {
        Customer customer = getCustomerById(id);
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phoneNumber(customer.getPhone())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CustomerResponse> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(
                customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .phoneNumber(customer.getPhone())
                        .build()
        ).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse update(Customer customer) {
        getCustomerById(customer.getId());
        customerRepository.saveAndFlush(customer);
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phoneNumber(customer.getPhone())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    private Customer getCustomerById(String id){
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found"));
    }
}
