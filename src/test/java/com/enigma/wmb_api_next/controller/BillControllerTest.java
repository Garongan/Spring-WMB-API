package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.dto.request.BillDetailRequest;
import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.dto.request.UpdateBillRequest;
import com.enigma.wmb_api_next.dto.response.BillDetailResponse;
import com.enigma.wmb_api_next.dto.response.BillResponse;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.PaymentResponse;
import com.enigma.wmb_api_next.service.BillService;
import com.enigma.wmb_api_next.service.Impl.PdfServiceImpl;
import com.enigma.wmb_api_next.service.PdfService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class BillControllerTest {
    @MockBean
    private BillService billService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "username")
    @Test
    void shouldHave201StatusAndReturnCommonResponseWhenCreateNewBill() throws Exception {
//        Given
        BillDetailRequest billDetailRequest = BillDetailRequest.builder()
                .menuId("menu-1")
                .qty(2)
                .price(2000L)
                .build();

        BillRequest billRequest = BillRequest.builder()
                .customerPhone("08123")
                .customerName("Cahyo")
                .tableName("Table 1")
                .transDate(new Date())
                .transType("DINE_IN")
                .billDetails(List.of(billDetailRequest))
                .build();

        BillDetailResponse billDetailResponse = BillDetailResponse.builder()
                .id("bill-detail-1")
                .menuId(billDetailRequest.getMenuId())
                .qty(billDetailRequest.getQty())
                .price(billDetailRequest.getPrice())
                .build();

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id("payment-1")
                .token("token-1")
                .transactionStatus("PENDING")
                .redirectUrl("http://localhost:8080/payment")
                .build();

        BillResponse billResponse = BillResponse.builder()
                .id("bill-1")
                .customerName(billRequest.getCustomerName())
                .transType(billRequest.getTransType())
                .transDate(billRequest.getTransDate().toString())
                .customerId("customer-1")
                .billdetails(List.of(billDetailResponse))
                .payment(paymentResponse)
                .build();

//        Stubbing
        Mockito.when(billService.save(Mockito.any())).thenReturn(billResponse);

//        When
        String stringJson = objectMapper.writeValueAsString(billRequest);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(ApiUrl.API_URL + ApiUrl.BILL_URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(stringJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(result -> {
                    CommonResponse<BillResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertEquals(201, response.getStatusCode());
                    assertEquals(StatusMessage.SUCCESS_CREATE, response.getMessage());
                    assertNotNull(response.getData());
                });
    }

    @WithMockUser(username = "username", roles = {"ADMIN"})
    @Test
    void shouldReturn200StatusAndReturnCommonResponseWhenGetById() throws Exception {
//        Given
        String id = "bill-1";
        BillResponse billResponse = BillResponse.builder()
                .id(id)
                .customerName("Cahyo")
                .transType("DINE_IN")
                .transDate(new Date().toString())
                .customerId("customer-1")
                .billdetails(List.of(BillDetailResponse.builder()
                        .id("bill-detail-1")
                        .menuId("menu-1")
                        .qty(2)
                        .price(2000L)
                        .build()))
                .payment(PaymentResponse.builder()
                        .id("payment-1")
                        .token("token-1")
                        .transactionStatus("PENDING")
                        .redirectUrl("http://localhost:8080/payment")
                        .build())
                .build();

//        Stubbing
        Mockito.when(billService.getById(id)).thenReturn(billResponse);

//        When
        mockMvc.perform(
                        MockMvcRequestBuilders.get(ApiUrl.API_URL + ApiUrl.BILL_URL + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<BillResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertEquals(200, response.getStatusCode());
                    assertEquals(StatusMessage.SUCCESS_RETRIEVE, response.getMessage());
                    assertNotNull(response.getData());
                });
    }

    @WithMockUser(username = "username", roles = {"ADMIN"})
    @Test
    void shouldReturn200StatusAndCommonResponseWhenGetAll() throws Exception {
//        Given
        Page<BillResponse> billResponsePage = new PageImpl<>(List.of(
                BillResponse.builder()
                        .id("bill-1")
                        .customerName("Cahyo")
                        .transType("DINE_IN")
                        .transDate(new Date().toString())
                        .customerId("customer-1")
                        .billdetails(List.of(BillDetailResponse.builder()
                                .id("bill-detail-1")
                                .menuId("menu-1")
                                .qty(2)
                                .price(2000L)
                                .build()))
                        .payment(PaymentResponse.builder()
                                .id("payment-1")
                                .token("token-1")
                                .transactionStatus("PENDING")
                                .redirectUrl("http://localhost:8080/payment")
                                .build())
                        .build()
        ));

//        Stubbing
        Mockito.when(billService.getAll(Mockito.any(SearchBillRequest.class))).thenReturn(billResponsePage);

//        When
        mockMvc.perform(
                        MockMvcRequestBuilders.get(ApiUrl.API_URL + ApiUrl.BILL_URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .param("daily", "2021-08-01")
                                .param("weeklyStart", "2021-08-01")
                                .param("weeklyEnd", "2021-08-07")
                                .param("monthly", "2021-08")
                                .param("direction", "asc")
                                .param("sortBy", "transDate")
                                .param("page", "1")
                                .param("size", "10")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<List<BillResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertEquals(200, response.getStatusCode());
                    assertEquals(StatusMessage.SUCCESS_RETRIEVE_LIST, response.getMessage());
                    assertNotNull(response.getData());
                });
    }

    @WithMockUser(username = "username", roles = {"ADMIN"})
    @Test
    void exportBillPdf() throws DocumentException, IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PdfService pdfService = Mockito.mock(PdfServiceImpl.class);
        pdfService.export(response);
        Mockito.verify(pdfService, Mockito.times(1)).export(response);

    }

    @Test
    void shouldUpdateStatusPaymentWhenCall() throws Exception {
//        Given
        UpdateBillRequest updateBillRequest = UpdateBillRequest.builder()
                .id("bill-1")
                .transactionStatus("PENDING")
                .build();

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id("payment-1")
                .token("token-1")
                .transactionStatus("PENDING")
                .redirectUrl("http://localhost:8080/payment")
                .build();

//        Stubbing
        Mockito.doNothing().when(billService).updateStatusPayment(updateBillRequest);

//        When
        mockMvc.perform(
                        MockMvcRequestBuilders.post(ApiUrl.API_URL + ApiUrl.BILL_URL + "/status")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"order_id\":\"bill-1\",\"transaction_status\":\"PENDING\"}")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertEquals(200, response.getStatusCode());
                    assertEquals(StatusMessage.SUCCESS_UPDATE, response.getMessage());
                });
    }
}