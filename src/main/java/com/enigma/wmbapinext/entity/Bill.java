package com.enigma.wmbapinext.entity;

import com.enigma.wmbapinext.constant.TableName;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = TableName.T_BILL)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "trans_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private TableMenu table;

    @ManyToOne
    @JoinColumn(name = "trans_type", nullable = false)
    private TransType transType;

    @OneToMany(mappedBy = "bill")
    @JsonManagedReference
    private List<BillDetail> billDetails;
}
