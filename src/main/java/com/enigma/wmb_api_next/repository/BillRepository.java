package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BillRepository extends JpaRepository<Bill, String>, JpaSpecificationExecutor<Bill> {
}
