package com.enigma.wmb_api_next.entity;

import com.enigma.wmb_api_next.constant.TableName;
import com.enigma.wmb_api_next.constant.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = TableName.M_USER_ROLE)
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}
