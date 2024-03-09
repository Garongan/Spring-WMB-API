package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.response.TransTypeResponse;

import java.util.List;

public interface TransTypeService {
    TransTypeResponse getById(String id);
    List<TransTypeResponse> getAll();
}
