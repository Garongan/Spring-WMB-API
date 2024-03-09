package com.enigma.wmb_api_next.entity;

import com.enigma.wmb_api_next.constant.TableName;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = TableName.M_MENU)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @OneToOne
    @JoinColumn(name = "image_id", unique = true)
    private Image image;
}
