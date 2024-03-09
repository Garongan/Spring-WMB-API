package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.TransTypeEnum;
import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.dto.response.*;
import com.enigma.wmb_api_next.entity.*;
import com.enigma.wmb_api_next.repository.BillRepository;
import com.enigma.wmb_api_next.service.*;
import com.enigma.wmb_api_next.specification.BillSpecification;
import com.enigma.wmb_api_next.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final BillSpecification specification;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BillResponse save(BillRequest request) {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name(request.getCustomerName())
                .phoneNumber(request.getCustomerPhone())
                .build();

        validationUtil.validate(customerRequest);

        CustomerResponse customerResponse = customerService.saveOrGet(customerRequest);
        Customer customer = Customer.builder()
                .id(customerResponse.getId())
                .name(customerResponse.getName())
                .phone(customerResponse.getPhoneNumber()).build();

        TransTypeResponse transTypeResponse = transTypeService.getById(request.getTransType());

        TableMenuResponse tableMenuResponse = tableMenuService.getByName(request.getTableName());
        TableMenu tableMenu = TableMenu.builder()
                .id(tableMenuResponse.getId())
                .name(tableMenuResponse.getName())
                .build();

        Bill bill = Bill.builder()
                .customer(customer)
                .transDate(new Date(Instant.now().toEpochMilli()))
                .table(tableMenu)
                .transType(TransType.builder()
                        .id(TransTypeEnum.valueOf(transTypeResponse.getId()))
                        .description(transTypeResponse.getDescription())
                        .build())
                .build();

        List<BillDetail> billDetails = request.getBillDetails().stream().map(
                billDetailRequest -> {
                    validationUtil.validate(billDetailRequest);
                    return BillDetail.builder()
                            .bill(bill)
                            .menu(menuService.getMenuById(billDetailRequest.getMenuId()))
                            .qty(billDetailRequest.getQty())
                            .price(billDetailRequest.getPrice())
                            .build();
                }
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

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BillResponse> getAll(SearchBillRequest request) {
        Specification<Bill> BillSpecification = specification.specification(request);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);

        Page<Bill> bills = billRepository.findAll(BillSpecification, pageable);
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
