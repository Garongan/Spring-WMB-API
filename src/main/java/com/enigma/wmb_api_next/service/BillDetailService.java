package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.BillDetail;

public interface BillDetailService {
    BillDetail save(BillDetail billDetail);

    BillDetail getById(String id);
}
