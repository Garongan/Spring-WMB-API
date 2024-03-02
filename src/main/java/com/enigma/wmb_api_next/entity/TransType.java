package com.enigma.wmb_api_next.entity;

import com.enigma.wmb_api_next.constant.TableName;
import com.enigma.wmb_api_next.constant.TransTypeEnum;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = TableName.M_TRANS_TYPE)
public class TransType {
    @Id
    @Enumerated(EnumType.STRING)
    private TransTypeEnum id;

    @Column(name = "description", nullable = false)
    private String description;
}
