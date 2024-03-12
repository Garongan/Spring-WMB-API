package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.TransTypeEnum;
import com.enigma.wmb_api_next.dto.request.BillDetailRequest;
import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.response.CustomerResponse;
import com.enigma.wmb_api_next.dto.response.TableMenuResponse;
import com.enigma.wmb_api_next.dto.response.TransTypeResponse;
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

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        customerService = Mockito.mock(CustomerService.class);
        transTypeService = Mockito.mock(TransTypeService.class);
        menuService = Mockito.mock(MenuService.class);
        tableMenuService = Mockito.mock(TableMenuService.class);
        specification = Mockito.mock(BillSpecification.class);
        validationUtil = Mockito.mock(ValidationUtil.class);
        paymentService = Mockito.mock(PaymentService.class);
    }

    @Test
    void shoulSaveBillWhenBillRequestGiven() {
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
    void getById() {
    }

    @Test
    void getAll() {
    }

    @Test
    void updateStatusPayment() {
    }
}