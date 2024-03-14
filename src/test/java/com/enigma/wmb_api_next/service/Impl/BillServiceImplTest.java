package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.TransTypeEnum;
import com.enigma.wmb_api_next.dto.request.*;
import com.enigma.wmb_api_next.dto.response.*;
import com.enigma.wmb_api_next.entity.*;
import com.enigma.wmb_api_next.repository.BillRepository;
import com.enigma.wmb_api_next.service.*;
import com.enigma.wmb_api_next.specification.BillSpecification;
import com.enigma.wmb_api_next.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {
    @Mock
    private BillRepository billRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private TransTypeService transTypeService;
    @Mock
    private MenuService menuService;
    @Mock
    private TableMenuService tableMenuService;
    @Mock
    private BillSpecification specification;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {

        customerService = mock(CustomerService.class);
        transTypeService = mock(TransTypeService.class);
        menuService = mock(MenuService.class);
        tableMenuService = mock(TableMenuService.class);
        specification = mock(BillSpecification.class);
        validationUtil = mock(ValidationUtil.class);
        paymentService = mock(PaymentService.class);
    }

    @Test
    void shouldSaveBillWhenBillRequestGiven() {
//        Given
        BillDetailRequest mockBillDetailRequest = BillDetailRequest.builder()
                .menuId("menu-id")
                .qty(1)
                .price(1000L)
                .build();

        BillRequest mockBillRequest = BillRequest.builder()
                .customerName("customer")
                .customerPhone("123")
                .tableName("table")
                .transType("transType")
                .billDetails(List.of(mockBillDetailRequest))
                .build();

        CustomerRequest mockCustomerRequest = CustomerRequest.builder()
                .name("customer")
                .phoneNumber("123")
                .build();

        CustomerResponse mockCustomerResponse = CustomerResponse.builder()
                .id("customer-id")
                .name("customer")
                .phoneNumber("123")
                .build();

        TransTypeResponse mockTransTypeResponse = TransTypeResponse.builder()
                .id(TransTypeEnum.EI.name())
                .description("Eat In").build();

        TableMenuResponse mockTableMenuResponse = TableMenuResponse.builder()
                .id("table-id")
                .name("table")
                .build();

        Menu mockMenu = Menu.builder()
                .id("menu-id")
                .name("menu")
                .price(1000L)
                .build();

        Customer mockCustomer = Customer.builder()
                .id(mockCustomerResponse.getId())
                .name(mockCustomerResponse.getName())
                .phone(mockCustomerResponse.getPhoneNumber()).build();

        TableMenu mockTableMenu = TableMenu.builder()
                .id(mockTableMenuResponse.getId())
                .name(mockTableMenuResponse.getName())
                .build();

        TransType mockTransType = TransType.builder()
                .id(TransTypeEnum.valueOf(mockTransTypeResponse.getId()))
                .description(mockTransTypeResponse.getDescription())
                .build();

        Bill mockBill = Bill.builder()
                .transDate(new Date())
                .customer(mockCustomer)
                .table(mockTableMenu)
                .transType(mockTransType)
                .build();

        BillDetail mockBillDetail = BillDetail.builder()
                .bill(mockBill)
                .menu(mockMenu)
                .qty(mockBillDetailRequest.getQty())
                .price(mockBillDetailRequest.getPrice())
                .build();

        mockBill.setBillDetails(List.of(mockBillDetail));


//        Stubbing
        Mockito.doNothing().when(validationUtil).validate(mockBillRequest);
        Mockito.doNothing().when(validationUtil).validate(mockBillDetailRequest);
        Mockito.when(customerService.saveOrGet(mockCustomerRequest)).thenReturn(mockCustomerResponse);
        Mockito.when(transTypeService.getById(mockBillRequest.getTransType())).thenReturn(mockTransTypeResponse);
        Mockito.when(tableMenuService.getByName(mockBillRequest.getTableName())).thenReturn(mockTableMenuResponse);
        Mockito.when(menuService.getMenuById(mockBillDetailRequest.getMenuId())).thenReturn(mockMenu);
        Mockito.when(paymentService.createPayment(mockBill)).thenReturn(mockBill.getPayment());
        Mockito.when(billRepository.save(mockBill)).thenReturn(mockBill);

//        When
        validationUtil.validate(mockBillRequest);
        validationUtil.validate(mockBillDetailRequest);
        CustomerResponse actualCustomerResponse = customerService.saveOrGet(mockCustomerRequest);
        TransTypeResponse actualTransTypeResponse = transTypeService.getById(mockBillRequest.getTransType());
        TableMenuResponse actualTableMenuResponse = tableMenuService.getByName(mockBillRequest.getTableName());
        Menu actualMenu = menuService.getMenuById(mockBillDetailRequest.getMenuId());
        Payment actualPayment = paymentService.createPayment(mockBill);
        Bill actual = billRepository.save(mockBill);

//        Then
        assertEquals(mockCustomerResponse, actualCustomerResponse);
        assertEquals(mockTransTypeResponse, actualTransTypeResponse);
        assertEquals(mockTableMenuResponse, actualTableMenuResponse);
        assertEquals(mockMenu, actualMenu);
        assertEquals(mockBill.getPayment(), actualPayment);
        assertEquals(mockBill, actual);
    }

    @Test
    void shouldReturnBillResponseWhenIdGiven() {
//        Given
        String mockId = "mock-id";

        Menu mockMenu = Menu.builder()
                .id("menu-id")
                .name("menu")
                .price(1000L)
                .build();

        Customer mockCustomer = Customer.builder()
                .id("customer-id")
                .name("customer")
                .phone("08999")
                .build();

        TableMenu mockTableMenu = TableMenu.builder()
                .id("table-id")
                .name("table")
                .build();

        TransType mockTransType = TransType.builder()
                .id(TransTypeEnum.EI)
                .description("Eat In")
                .build();


        Bill mockBill = Bill.builder()
                .transDate(new Date())
                .customer(mockCustomer)
                .table(mockTableMenu)
                .transType(mockTransType)
                .build();

        BillDetail mockBillDetail = BillDetail.builder()
                .id("bill-detail-id")
                .bill(mockBill)
                .menu(mockMenu)
                .qty(1)
                .price(1000L)
                .build();

        Payment mockPayment = Payment.builder()
                .id("payment-id")
                .tokan("token")
                .bill(mockBill)
                .transactionStatus("transaction-status")
                .redirectUrl("redirect-url")
                .build();

        mockBill.setPayment(mockPayment);

        mockBill.setBillDetails(List.of(mockBillDetail));

        BillDetailResponse mockBillDetailResponse = BillDetailResponse.builder()
                .id(mockBillDetail.getId())
                .menuId(mockBillDetail.getMenu().getId())
                .price(mockBillDetail.getPrice())
                .qty(mockBillDetail.getQty())
                .build();

        PaymentResponse mockPaymentResponse = PaymentResponse.builder()
                .id("payment-id")
                .token("token")
                .redirectUrl("redirect-url")
                .transactionStatus("transaction-status")
                .build();

        BillResponse mockBillResponse = BillResponse.builder()
                .id(mockBill.getId())
                .transDate(mockBill.getTransDate().toString())
                .customerId(mockBill.getCustomer().getId())
                .customerName(mockBill.getCustomer().getName())
                .tableName(mockBill.getTable().getName())
                .billdetails(List.of(mockBillDetailResponse))
                .transType(mockBill.getTransType().getId().name())
                .payment(mockPaymentResponse)
                .build();

//        Stubbing config
        Mockito.when(billRepository.findById(mockId))
                .thenReturn(Optional.of(mockBill));

//        When
        Bill actualBill = billRepository.findById(mockId).orElseThrow();
        BillResponse actualBillResponse = BillResponse.builder()
                .id(actualBill.getId())
                .transDate(actualBill.getTransDate().toString())
                .customerId(actualBill.getCustomer().getId())
                .customerName(actualBill.getCustomer().getName())
                .tableName(actualBill.getTable().getName())
                .billdetails(List.of(mockBillDetailResponse))
                .transType(actualBill.getTransType().getId().name())
                .payment(mockPaymentResponse)
                .build();

//        Then
        assertNotNull(actualBill);
        assertEquals(mockBillResponse.getId(), actualBillResponse.getId());
        assertEquals(mockBillResponse.getTransDate(), actualBillResponse.getTransDate());
        assertEquals(mockBillResponse.getCustomerId(), actualBillResponse.getCustomerId());
        assertEquals(mockBillResponse.getCustomerName(), actualBillResponse.getCustomerName());
        assertEquals(mockBillResponse.getTableName(), actualBillResponse.getTableName());
        assertEquals(mockBillResponse.getBilldetails(), actualBillResponse.getBilldetails());
        assertEquals(mockBillResponse.getTransType(), actualBillResponse.getTransType());
        assertEquals(mockBillResponse.getPayment(), actualBillResponse.getPayment());
    }

    @Test
    void shouldReturnAllBillResponsePageWhenGiven() {
//        Given
        SearchBillRequest mockSearchBillRequest = SearchBillRequest.builder()
                .daily("2021-08-01")
                .weeklyStart("2021-08-01")
                .weeklyEnd("2021-08-07")
                .monthly("2021-08")
                .direction("asc")
                .sortBy("transDate")
                .page(1)
                .size(10)
                .build();

        Menu mockMenu = Menu.builder()
                .id("menu-id")
                .name("menu")
                .price(1000L)
                .build();

        Customer mockCustomer = Customer.builder()
                .id("customer-id")
                .name("customer")
                .phone("08999")
                .build();

        TableMenu mockTableMenu = TableMenu.builder()
                .id("table-id")
                .name("table")
                .build();

        TransType mockTransType = TransType.builder()
                .id(TransTypeEnum.EI)
                .description("Eat In")
                .build();

        Bill mockBill = Bill.builder()
                .transDate(new Date())
                .customer(mockCustomer)
                .table(mockTableMenu)
                .transType(mockTransType)
                .build();

        Payment mockPayment = Payment.builder()
                .id("payment-id")
                .redirectUrl("redirect-url")
                .tokan("token")
                .transactionStatus("transaction-status")
                .bill(mockBill)
                .build();

        mockBill.setPayment(mockPayment);

        BillDetail mockBillDetail = BillDetail.builder()
                .bill(mockBill)
                .menu(mockMenu)
                .qty(1)
                .price(1000L)
                .build();

        mockBill.setBillDetails(List.of(mockBillDetail));

        Page<Bill> mockBills = new PageImpl<>(List.of(mockBill));
        Specification<Bill> billSpecification = mock(Specification.class);

        Sort sort = Sort.by(Sort.Direction.ASC, "transDate");
        Pageable pageable = PageRequest.of(1, 10, sort);

//        Stubbing config
        Mockito.when(billRepository.findAll(billSpecification, pageable))
                .thenReturn(mockBills);

//        When
        Page<Bill> actualBills = billRepository.findAll(billSpecification, pageable);

//        Then
        assertNotNull(actualBills);
        assertEquals(mockBills.getTotalElements(), actualBills.getTotalElements());
        assertEquals(mockBills.getTotalPages(), actualBills.getTotalPages());
        assertEquals(mockBills.getContent(), actualBills.getContent());
    }

    @Test
    void shouldUpdateStatusPaymentWhenGiven() {
//        Given
        UpdateBillRequest mockUpdateBillRequest = UpdateBillRequest.builder()
                .id("bill-id")
                .transactionStatus("transaction-status")
                .build();

        Menu mockMenu = Menu.builder()
                .id("menu-id")
                .name("menu")
                .price(1000L)
                .build();

        Customer mockCustomer = Customer.builder()
                .id("customer-id")
                .name("customer")
                .phone("08999")
                .build();

        TableMenu mockTableMenu = TableMenu.builder()
                .id("table-id")
                .name("table")
                .build();

        TransType mockTransType = TransType.builder()
                .id(TransTypeEnum.EI)
                .description("Eat In")
                .build();

        Bill mockBill = Bill.builder()
                .transDate(new Date())
                .customer(mockCustomer)
                .table(mockTableMenu)
                .transType(mockTransType)
                .build();

        Payment mockPayment = Payment.builder()
                .id("payment-id")
                .redirectUrl("redirect-url")
                .tokan("token")
                .transactionStatus("transaction-status")
                .bill(mockBill)
                .build();

        mockBill.setPayment(mockPayment);

        BillDetail mockBillDetail = BillDetail.builder()
                .bill(mockBill)
                .menu(mockMenu)
                .qty(1)
                .price(1000L)
                .build();

        mockBill.setBillDetails(List.of(mockBillDetail));

//        Stubbing config
        Mockito.when(billRepository.findById(mockUpdateBillRequest.getId())).thenReturn(Optional.of(mockBill));

//        When
        Bill actualBill = billRepository.findById(mockUpdateBillRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill Not Found"));
        Payment actualPayment = mockBill.getPayment();
        actualPayment.setTransactionStatus(mockUpdateBillRequest.getTransactionStatus());

//        Then
        assertNotNull(actualBill);
        assertEquals(mockUpdateBillRequest.getTransactionStatus(), actualPayment.getTransactionStatus());
    }
}