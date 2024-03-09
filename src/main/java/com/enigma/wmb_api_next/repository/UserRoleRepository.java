package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import com.enigma.wmb_api_next.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    Optional<UserRole> findByRole(UserRoleEnum role);
}
