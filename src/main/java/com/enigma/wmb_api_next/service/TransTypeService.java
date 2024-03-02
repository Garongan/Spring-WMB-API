package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.TransType;

public interface TransTypeService {
    TransType getById(String Id);
    TransType getAll();
}
