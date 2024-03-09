package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import com.enigma.wmb_api_next.entity.UserRole;

public interface UserRoleService {
    UserRole saveOrGet(UserRoleEnum userRoleEnum);
}
