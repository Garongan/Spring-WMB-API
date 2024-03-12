package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import com.enigma.wmb_api_next.entity.*;
import com.enigma.wmb_api_next.repository.BillDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillDetailServiceImplTest {
    @Mock
    private BillDetailRepository billDetailRepository;

    @BeforeEach
    void setUp() {
        billDetailRepository = Mockito.mock(BillDetailRepository.class);
    }

    @Test
    void shouldCreateBillDetailWhenSave() {
//        Given
        UserRole superAdminRole = UserRole.builder()
                .id("super-admin-role-id")
                .role(UserRoleEnum.SUPER_ADMIN)
                .build();
        UserRole adminRole = UserRole.builder()
                .id("admin-role-id")
                .role(UserRoleEnum.ADMIN).build();
        UserRole userRole = UserRole.builder()
                .id("user-role-id")
                .role(UserRoleEnum.USER).build();

        UserAccount mockUserAccount = UserAccount.builder()
                .id("user-id")
                .username("username")
                .password("password")
                .roles(List.of(superAdminRole, adminRole, userRole))
                .isEnable(true)
                .build();

        Customer mockCustomer = Customer.builder()
                .id("customer-id")
                .name("customer-name")
                .phone("089990")
                .userAccount(mockUserAccount)
                .build();

        Bill mockBill = Bill.builder()
                .id("bill-id")
                .transDate(new Date())
                .customer(mockCustomer)
                .build();

        Image mockImage = Image.builder()
                .id("image-id")
                .name("image-name")
                .path("image-path")
                .build();

        Menu mockMenu = Menu.builder()
                .name("menu-name")
                .price(2000L)
                .image(mockImage)
                .build();

        BillDetail mockBillDetail = BillDetail.builder()
                .id("bill-detail-id")
                .bill(mockBill)
                .menu(mockMenu)
                .price(2000L)
                .qty(3)
                .build();

//        Stubbing config
        Mockito.when(billDetailRepository.saveAndFlush(mockBillDetail)).thenReturn(mockBillDetail);

//        When
        BillDetail actualBillDetail = billDetailRepository.saveAndFlush(mockBillDetail);

//        Then
        assertNotNull(actualBillDetail);
        assertEquals(mockBillDetail.getId(), actualBillDetail.getId());
        assertEquals(mockBillDetail.getBill().getId(), actualBillDetail.getBill().getId());
        assertEquals(mockBillDetail.getMenu(), actualBillDetail.getMenu());
        assertEquals(mockBillDetail.getPrice(), actualBillDetail.getPrice());
        assertEquals(mockBillDetail.getQty(), actualBillDetail.getQty());

    }

    @Test
    void shouldReturnBillDetailWhenGivingIdIsFound() {
//        Given
        String id = "bill-detail-id";

        UserRole superAdminRole = UserRole.builder()
                .id("super-admin-role-id")
                .role(UserRoleEnum.SUPER_ADMIN)
                .build();
        UserRole adminRole = UserRole.builder()
                .id("admin-role-id")
                .role(UserRoleEnum.ADMIN).build();
        UserRole userRole = UserRole.builder()
                .id("user-role-id")
                .role(UserRoleEnum.USER).build();

        UserAccount mockUserAccount = UserAccount.builder()
                .id("user-id")
                .username("username")
                .password("password")
                .roles(List.of(superAdminRole, adminRole, userRole))
                .isEnable(true)
                .build();

        Customer mockCustomer = Customer.builder()
                .id("customer-id")
                .name("customer-name")
                .phone("089990")
                .userAccount(mockUserAccount)
                .build();

        Bill mockBill = Bill.builder()
                .id("bill-id")
                .transDate(new Date())
                .customer(mockCustomer)
                .build();

        Image mockImage = Image.builder()
                .id("image-id")
                .name("image-name")
                .path("image-path")
                .build();

        Menu mockMenu = Menu.builder()
                .name("menu-name")
                .price(2000L)
                .image(mockImage)
                .build();

        BillDetail mockBillDetail = BillDetail.builder()
                .id(id)
                .bill(mockBill)
                .menu(mockMenu)
                .price(2000L)
                .qty(3)
                .build();

//        Stubbing config
        Mockito.when(billDetailRepository.findById(id)).thenReturn(java.util.Optional.of(mockBillDetail));

//        When
        BillDetail actualBillDetail = billDetailRepository.findById(id).orElse(null);

//        Then
        assertNotNull(actualBillDetail);
        assertEquals(mockBillDetail.getId(), actualBillDetail.getId());
        assertEquals(mockBillDetail.getBill().getId(), actualBillDetail.getBill().getId());
        assertEquals(mockBillDetail.getMenu(), actualBillDetail.getMenu());
        assertEquals(mockBillDetail.getPrice(), actualBillDetail.getPrice());
        assertEquals(mockBillDetail.getQty(), actualBillDetail.getQty());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenGivingIdNotFound() {
//        Given
        String wrongId = "not-bill-detail-id";

//        Stubbing config
        Mockito.when(billDetailRepository.findById(wrongId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "BIll Detail Not Found"));

//        When
        try {
            billDetailRepository.findById(wrongId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "BIll Detail Not Found"));

            fail("Should throw ResponseStatusException when giving id not found");
        }catch (ResponseStatusException e){
//        Then
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertEquals("BIll Detail Not Found", e.getReason());
        }

    }
}