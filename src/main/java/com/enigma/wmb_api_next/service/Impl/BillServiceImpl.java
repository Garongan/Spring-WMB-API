package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.constant.TransTypeEnum;
import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.dto.request.UpdateBillRequest;
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
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailService billDetailService;
    private final CustomerService customerService;
    private final TransTypeService transTypeService;
    private final MenuService menuService;
    private final TableMenuService tableMenuService;
    private final BillSpecification specification;
    private final ValidationUtil validationUtil;
    private final PaymentService paymentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BillResponse save(BillRequest request) {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name(request.getCustomerName())
                .phoneNumber(request.getCustomerPhone())
                .build();

        validationUtil.validate(customerRequest);

        Customer customer;
        Customer foundedCustomer = customerService.getCustomerByNameAndPhone(request.getCustomerName(), request.getCustomerPhone());

        if (foundedCustomer == null) {
            customer = customerService.save(customerRequest);
        } else {
            customer = foundedCustomer;
        }

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
        billRepository.saveAndFlush(bill);

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

        Payment payment = paymentService.createPayment(bill);
        bill.setPayment(payment);

        return BillResponse.builder()
                .id(bill.getId())
                .transType(bill.getTransType().getId().name())
                .customerId(bill.getCustomer().getId())
                .customerName(bill.getCustomer().getName())
                .tableName(bill.getTable().getName())
                .transDate(bill.getTransDate())
                .billdetails(bill.getBillDetails().stream().map(
                        billDetail -> BillDetailResponse.builder()
                                .id(billDetail.getId())
                                .menuId(billDetail.getMenu().getId())
                                .qty(billDetail.getQty())
                                .price(billDetail.getPrice())
                                .build()
                ).toList())
                .payment(PaymentResponse.builder()
                        .id(payment.getId())
                        .token(payment.getTokan())
                        .transactionStatus(payment.getTransactionStatus())
                        .redirectUrl(payment.getRedirectUrl())
                        .build())
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
    public Page<BillResponse> getAll(SearchBillRequest request) {
        Specification<Bill> BillSpecification = specification.specification(request);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);

        Page<Bill> bills = billRepository.findAll(BillSpecification, pageable);
        return bills.map(
                bill -> {
                    BillResponse.BillResponseBuilder billResponseBuilder = BillResponse.builder()
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
                            ).toList());
                    if (bill.getPayment() != null)
                        billResponseBuilder.payment(PaymentResponse.builder()
                                .id(bill.getPayment().getId())
                                .token(bill.getPayment().getTokan())
                                .transactionStatus(bill.getPayment().getTransactionStatus())
                                .redirectUrl(bill.getPayment().getRedirectUrl())
                                .build()
                        );
                    return billResponseBuilder.build();
                }
        );
    }

    @Override
    public List<BillResponse> exportAll() {
        List<Bill> bills = billRepository.findAll();
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusPayment(UpdateBillRequest request) {
        Bill bill = billRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.NOT_FOUND));
        Payment payment = bill.getPayment();
        payment.setTransactionStatus(request.getTransactionStatus());
    }
}
