package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.dto.response.BillResponse;

import java.util.List;

public interface BillService {
    BillResponse save(BillRequest request);

    BillResponse getById(String id);

    List<BillResponse> getAll(SearchBillRequest request);
}
