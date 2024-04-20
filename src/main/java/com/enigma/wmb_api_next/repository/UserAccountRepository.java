package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.entity.UserAccount;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    @Cacheable("userAccounts")
    Optional<UserAccount> findByUsername(String username);
}
