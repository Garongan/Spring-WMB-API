package com.enigma.wmb_api_next.scheduler;

import com.enigma.wmb_api_next.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusScheduler {
    private final PaymentService paymentService;

    @Scheduled(fixedRate = 60000, initialDelay = 10000)
    public void checkFailedPayment() {
        log.info("START checkFailedPayment(): {}", System.currentTimeMillis());
        paymentService.checkFailedAndUpdatePaymentStatus();
        log.info("END checkFailedPayment(): {}", System.currentTimeMillis());
    }
}
