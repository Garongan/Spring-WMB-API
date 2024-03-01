package com.enigma.wmbapinext.repository;

import com.enigma.wmbapinext.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, String> {
}
