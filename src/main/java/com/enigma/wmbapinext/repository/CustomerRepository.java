package com.enigma.wmbapinext.repository;

import com.enigma.wmbapinext.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
