package com.enigma.wmbapinext.service;

import com.enigma.wmbapinext.entity.BillDetail;

public interface BillDetailService {
    BillDetail save(BillDetail billDetail);

    BillDetail getById(String id);
}
