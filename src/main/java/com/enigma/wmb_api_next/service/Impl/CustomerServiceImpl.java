package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.request.NewAccountRequest;
import com.enigma.wmb_api_next.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api_next.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api_next.dto.response.CustomerResponse;
import com.enigma.wmb_api_next.entity.Customer;
import com.enigma.wmb_api_next.repository.CustomerRepository;
import com.enigma.wmb_api_next.service.CustomerService;
import com.enigma.wmb_api_next.specification.CustomerSpecification;
import com.enigma.wmb_api_next.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final CustomerSpecification specification;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse saveOrGet(CustomerRequest request) {
        validationUtil.validate(request);
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
    public Customer saveAccount(NewAccountRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .userAccount(request.getUserAccount())
                .build();
        return customerRepository.saveAndFlush(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CustomerResponse> saveBulk(List<CustomerRequest> requests) {
        validationUtil.validate(requests);
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
    public List<CustomerResponse> getAll(SearchCustomerRequest request) {
        Specification<Customer> CustomerSpecification = specification.specification(request.getName());

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), sort);

        Page<Customer> customers = customerRepository.findAll(CustomerSpecification, pageable);
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
    public CustomerResponse update(UpdateCustomerRequest request) {
        validationUtil.validate(request);
        getCustomerById(request.getId());
        customerRepository.saveAndFlush(
                Customer.builder()
                        .id(request.getId())
                        .name(request.getName())
                        .phone(request.getPhone())
                        .build()
        );
        return CustomerResponse.builder()
                .id(request.getId())
                .name(request.getName())
                .phoneNumber(request.getPhone())
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
