package com.enigma.wmbapinext.entity;

import com.enigma.wmbapinext.constant.TableName;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = TableName.M_TABLE)
public class TableMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;
}
