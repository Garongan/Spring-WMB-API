package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.Bill;

import java.util.List;

public interface BillService {
    Bill save(Bill bill);

    Bill getById(String id);

    List<Bill> getAll();
}
