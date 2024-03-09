package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.response.PaymentResponse;
import com.enigma.wmb_api_next.entity.Bill;
import com.enigma.wmb_api_next.entity.Payment;

public interface PaymentService {
    Payment createPayment(Bill bill);
    PaymentResponse getPaymentStatus(String paymentId);
    void checkFailedAndUpdatePaymentStatus();
}
