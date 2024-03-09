package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import com.enigma.wmb_api_next.entity.UserRole;
import com.enigma.wmb_api_next.repository.UserRoleRepository;
import com.enigma.wmb_api_next.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRole saveOrGet(UserRoleEnum userRoleEnum) {
        return userRoleRepository.findByRole(userRoleEnum)
                .orElseGet(() -> userRoleRepository.saveAndFlush(UserRole.builder().role(userRoleEnum).build()));
    }
}
