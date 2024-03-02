package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
