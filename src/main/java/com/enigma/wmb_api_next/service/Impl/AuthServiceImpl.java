package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import com.enigma.wmb_api_next.dto.request.LoginRequest;
import com.enigma.wmb_api_next.dto.request.NewAccountRequest;
import com.enigma.wmb_api_next.dto.request.RegisterRequest;
import com.enigma.wmb_api_next.dto.response.LoginResponse;
import com.enigma.wmb_api_next.dto.response.RegisterResponse;
import com.enigma.wmb_api_next.entity.Customer;
import com.enigma.wmb_api_next.entity.UserAccount;
import com.enigma.wmb_api_next.entity.UserRole;
import com.enigma.wmb_api_next.repository.UserAccountRepository;
import com.enigma.wmb_api_next.service.*;
import com.enigma.wmb_api_next.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationUtil validationUtil;

    @Value("${wmb_api_next.super-admin.username}")
    private String superAdminUsername;
    @Value("${wmb_api_next.super-admin.password}")
    private String superAdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin() {
        Optional<UserAccount> account = userAccountRepository.findByUsername(superAdminUsername);

        if (account.isPresent()) return;
        UserRole superAdmin = userRoleService.saveOrGet(UserRoleEnum.ROLE_SUPER_ADMIN);
        UserRole admin = userRoleService.saveOrGet(UserRoleEnum.ROLE_ADMIN);
        UserRole user = userRoleService.saveOrGet(UserRoleEnum.ROLE_USER);

        userAccountRepository.saveAndFlush(UserAccount.builder()
                .username(superAdminUsername)
                .password(passwordEncoder.encode(superAdminPassword))
                .roles(List.of(superAdmin, admin, user))
                .isEnable(true)
                .build());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        validationUtil.validate(request);
        UserRole roleUser = userRoleService.saveOrGet(UserRoleEnum.ROLE_USER);
        return getRegisterResponse(request, List.of(roleUser));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerAdmin(RegisterRequest request) {
        validationUtil.validate(request);
        UserRole roleUser = userRoleService.saveOrGet(UserRoleEnum.ROLE_ADMIN);
        UserRole roleAdmin = userRoleService.saveOrGet(UserRoleEnum.ROLE_USER);
        return getRegisterResponse(request, List.of(roleUser, roleAdmin));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginResponse login(LoginRequest request) {
        validationUtil.validate(request);
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .username(userAccount.getUsername())
                .token(token)
                .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    private RegisterResponse getRegisterResponse(RegisterRequest request, List<UserRole> userRoles) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserAccount userAccount = userAccountRepository.saveAndFlush(UserAccount.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .roles(userRoles)
                .isEnable(true)
                .build());

        Customer customer = customerService.saveAccount(NewAccountRequest.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .userAccount(userAccount)
                .build());

        List<String> roles = customer.getUserAccount().getRoles().stream()
                .map(role -> role.getRole().name())
                .toList();

        return RegisterResponse.builder()
                .username(userAccount.getUsername())
                .roles(roles)
                .build();
    }
}
