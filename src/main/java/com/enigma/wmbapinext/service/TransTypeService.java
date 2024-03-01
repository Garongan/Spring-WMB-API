package com.enigma.wmbapinext.service;

import com.enigma.wmbapinext.entity.TransType;

public interface TransTypeService {
    TransType getById(String Id);
    TransType getAll();
}
