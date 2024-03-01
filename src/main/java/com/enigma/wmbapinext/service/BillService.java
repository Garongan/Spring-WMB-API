package com.enigma.wmbapinext.service;

import com.enigma.wmbapinext.entity.Bill;

import java.util.List;

public interface BillService {
    Bill save(Bill bill);

    Bill getById(String id);

    List<Bill> getAll();
}
