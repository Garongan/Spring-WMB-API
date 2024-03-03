package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.response.BillDetailResponse;
import com.enigma.wmb_api_next.dto.response.BillResponse;
import com.enigma.wmb_api_next.entity.*;
import com.enigma.wmb_api_next.repository.BillRepository;
import com.enigma.wmb_api_next.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final CustomerService customerService;
    private final TransTypeService transTypeService;
    private final MenuService menuService;
    private final TableMenuService tableMenuService;

    @Override
    public BillResponse save(BillRequest request) {
        Customer customer = customerService.saveOrGet(request.getCustomerName());

        Bill bill = Bill.builder()
                .customer(customer)
                .transDate(new Date(Instant.now().toEpochMilli()))
                .table(tableMenuService.getByName(request.getTableName()))
                .transType(transTypeService.getById(request.getTransType()))
                .build();

        List<BillDetail> billDetails = request.getBillDetails().stream().map(
                billDetailRequest -> BillDetail.builder()
                        .bill(bill)
                        .menu(menuService.getById(billDetailRequest.getMenuId()))
                        .qty(billDetailRequest.getQty())
                        .price(billDetailRequest.getPrice())
                        .build()
        ).toList();

        bill.setBillDetails(billDetails);

        Bill saved = billRepository.saveAndFlush(bill);

        return BillResponse.builder()
                .id(saved.getId())
                .transType(saved.getTransType().getId().name())
                .customerId(saved.getCustomer().getId())
                .customerName(saved.getCustomer().getName())
                .tableName(saved.getTable().getName())
                .transDate(saved.getTransDate())
                .billdetails(saved.getBillDetails().stream().map(
                        billDetail -> BillDetailResponse.builder()
                                .id(billDetail.getId())
                                .menuId(billDetail.getMenu().getId())
                                .qty(billDetail.getQty())
                                .price(billDetail.getPrice())
                                .build()
                ).toList())
                .build();
    }

    @Override
    public BillResponse getById(String id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill Not Found"));
        List<BillDetailResponse> billDetailResponses = bill.getBillDetails().stream().map(
                billDetail -> BillDetailResponse.builder()
                        .id(billDetail.getId())
                        .menuId(billDetail.getMenu().getId())
                        .qty(billDetail.getQty())
                        .price(billDetail.getPrice())
                        .build()
        ).toList();
        return BillResponse.builder()
                .id(bill.getId())
                .customerId(bill.getCustomer().getId())
                .customerName(bill.getCustomer().getName())
                .transDate(Date.from(Instant.now()))
                .tableName(bill.getTable().getName())
                .transType(bill.getTransType().getDescription())
                .billdetails(billDetailResponses)
                .build();
    }

    @Override
    public List<BillResponse> getAll() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream().map(
                bill -> BillResponse.builder()
                        .id(bill.getId())
                        .customerId(bill.getCustomer().getId())
                        .customerName(bill.getCustomer().getName())
                        .transDate(bill.getTransDate())
                        .tableName(bill.getTable().getName())
                        .transType(bill.getTransType().getDescription())
                        .billdetails(bill.getBillDetails().stream().map(
                                billDetail -> BillDetailResponse.builder()
                                        .id(billDetail.getId())
                                        .menuId(billDetail.getMenu().getId())
                                        .qty(billDetail.getQty())
                                        .price(billDetail.getPrice())
                                        .build()
                        ).toList())
                        .build()
        ).toList();
    }
}
