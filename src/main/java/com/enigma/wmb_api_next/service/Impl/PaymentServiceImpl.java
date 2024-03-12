package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.request.PaymentDetailRequest;
import com.enigma.wmb_api_next.dto.request.PaymentItemDetailRequest;
import com.enigma.wmb_api_next.dto.request.PaymentRequest;
import com.enigma.wmb_api_next.dto.response.PaymentResponse;
import com.enigma.wmb_api_next.entity.Bill;
import com.enigma.wmb_api_next.entity.Payment;
import com.enigma.wmb_api_next.repository.PaymentRepository;
import com.enigma.wmb_api_next.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final String BASE_URL;
    private final String SECRET_KEY;
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;

    public PaymentServiceImpl(
            @Value("${midtrans.snap.url}") String BASE_URL,
            @Value("${midtrans.api.key}") String SECRET_KEY,
            PaymentRepository paymentRepository,
            RestClient restClient
    ) {
        this.BASE_URL = BASE_URL;
        this.SECRET_KEY = SECRET_KEY;
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment  createPayment(Bill bill) {
        long amount = bill.getBillDetails().stream().mapToLong(
                        value -> (value.getPrice() * value.getQty()))
                .reduce(0, Long::sum);

        PaymentDetailRequest paymentDetailRequest = PaymentDetailRequest.builder()
                .id(bill.getId())
                .amount(amount)
                .build();

        List<PaymentItemDetailRequest> paymentItemDetailRequests = bill.getBillDetails().stream().map(billDetail -> PaymentItemDetailRequest.builder()
                .id(billDetail.getMenu().getId())
                .name(billDetail.getMenu().getName())
                .price(billDetail.getPrice())
                .quantity(billDetail.getQty())
                .build()
        ).toList();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentDetail(paymentDetailRequest)
                .paymentItemDetails(paymentItemDetailRequests)
                .enabledPayments(List.of("shopeepay", "gopay"))
                .build();

        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .body(paymentRequest)
                .retrieve().toEntity(new ParameterizedTypeReference<>() {
                });

        Map<String, String> responseBody = response.getBody();

        assert responseBody != null;
        Payment payment = Payment.builder()
                .bill(bill)
                .tokan(responseBody.get("token"))
                .redirectUrl(responseBody.get("redirect_url"))
                .transactionStatus("ordered")
                .build();
        paymentRepository.saveAndFlush(payment);
        return payment;
    }

    @Override
    public PaymentResponse getPaymentStatus(String paymentId) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkFailedAndUpdatePaymentStatus() {
        List<Payment> failedPaymentList = paymentRepository.findAllByTransactionStatusIn(List.of("failure", "expire", "deny", "cancel"));
        for (Payment payment : failedPaymentList) {
            payment.setTransactionStatus("returned");
        }
    }
}
