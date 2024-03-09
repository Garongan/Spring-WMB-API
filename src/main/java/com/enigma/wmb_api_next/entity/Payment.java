package com.enigma.wmb_api_next.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "token")
    private String tokan;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @OneToOne(mappedBy = "payment")
    private Bill bill;
}
