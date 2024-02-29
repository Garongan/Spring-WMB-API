package com.enigma.wmbapinext.entity;

import com.enigma.wmbapinext.constant.TableName;
import com.enigma.wmbapinext.constant.TransTypeEnum;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "description", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransTypeEnum transType;
}
