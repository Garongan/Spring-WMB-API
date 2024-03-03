package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.TransType;

import java.util.List;

public interface TransTypeService {
    TransType getById(String id);
    List<TransType> getAll();
}
