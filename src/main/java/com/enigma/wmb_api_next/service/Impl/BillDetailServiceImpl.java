package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.entity.BillDetail;
import com.enigma.wmb_api_next.repository.BillDetailRepository;
import com.enigma.wmb_api_next.service.BillDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BillDetailServiceImpl implements BillDetailService {
    private final BillDetailRepository billDetailRepository;
    @Override
    public BillDetail save(BillDetail billDetail) {
        return billDetailRepository.saveAndFlush(billDetail);
    }

    @Override
    public BillDetail getById(String id) {
        return billDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "BIll Detail Not Found"));
    }
}
