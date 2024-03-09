package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByNameLikeIgnoreCaseAndPhoneEquals(String name, String phone);
}
