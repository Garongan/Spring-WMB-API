package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByNameLikeIgnoreCase(String name);
}
