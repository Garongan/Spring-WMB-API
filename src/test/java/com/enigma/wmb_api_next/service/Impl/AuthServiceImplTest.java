package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import com.enigma.wmb_api_next.dto.request.LoginRequest;
import com.enigma.wmb_api_next.dto.request.NewAccountRequest;
import com.enigma.wmb_api_next.dto.request.RegisterRequest;
import com.enigma.wmb_api_next.dto.response.LoginResponse;
import com.enigma.wmb_api_next.entity.Customer;
import com.enigma.wmb_api_next.entity.UserAccount;
import com.enigma.wmb_api_next.entity.UserRole;
import com.enigma.wmb_api_next.repository.UserAccountRepository;
import com.enigma.wmb_api_next.service.CustomerService;
import com.enigma.wmb_api_next.service.JwtService;
import com.enigma.wmb_api_next.service.UserRoleService;
import com.enigma.wmb_api_next.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerService customerService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Value("${wmb_api_next.super-admin.username}")
    private String superAdminUsername;
    @Value("${wmb_api_next.super-admin.password}")
    private String superAdminPassword;

    @Mock
    private JwtService jwtService;
    @Mock
    private ValidationUtil validationUtil;

    @BeforeEach
    void setUp() {
        userRoleService = Mockito.mock(UserRoleService.class);
        customerService = Mockito.mock(CustomerService.class);
        jwtService = Mockito.mock(JwtService.class);
    }

    @Test
    void shouldFindSuperAdminWhenInit() {
//        Given
        UserRole mockSuperAdmin = UserRole.builder()
                .id("role-1")
                .role(UserRoleEnum.SUPER_ADMIN)
                .build();
        UserRole mockAdmin = UserRole.builder()
                .id("role-2")
                .role(UserRoleEnum.ADMIN)
                .build();
        UserRole mockUser = UserRole.builder()
                .id("role-3")
                .role(UserRoleEnum.USER)
                .build();

        UserAccount mockSuperAdminAccount = UserAccount.builder()
                .id("super-admin-id")
                .username(superAdminUsername)
                .password(superAdminPassword)
                .roles(List.of(mockSuperAdmin, mockAdmin, mockUser))
                .build();


//        Stubbing config
        Mockito.when(userAccountRepository.findByUsername(superAdminUsername)).thenReturn(Optional.of(mockSuperAdminAccount));

//        When
        Optional<UserAccount> actualSuperAdminAccount = userAccountRepository.findByUsername(superAdminUsername);

//        Then
        assertNotNull(actualSuperAdminAccount);
        actualSuperAdminAccount.ifPresent(userAccount -> {
            assertEquals(mockSuperAdminAccount.getId(), userAccount.getId());
            assertEquals(mockSuperAdminAccount.getUsername(), userAccount.getUsername());
            assertEquals(mockSuperAdminAccount.getPassword(), userAccount.getPassword());
            assertEquals(mockSuperAdminAccount.getRoles(), userAccount.getRoles());
            assertEquals(mockSuperAdminAccount.getIsEnable(), userAccount.getIsEnable());
        });
    }

    @Test
    void shouldCreateSuperAdminWhenInit() {
//        Given
        UserRole mockSuperAdmin = UserRole.builder()
                .id("role-1")
                .role(UserRoleEnum.SUPER_ADMIN)
                .build();
        UserRole mockAdmin = UserRole.builder()
                .id("role-2")
                .role(UserRoleEnum.ADMIN)
                .build();
        UserRole mockUser = UserRole.builder()
                .id("role-3")
                .role(UserRoleEnum.USER)
                .build();

        UserAccount mockSuperAdminAccount = UserAccount.builder()
                .id("super-admin-id")
                .username(superAdminUsername)
                .password(superAdminPassword)
                .roles(List.of(mockSuperAdmin, mockAdmin, mockUser))
                .build();


//        Stubbing config
        Mockito.when(userAccountRepository.saveAndFlush(mockSuperAdminAccount)).thenReturn(mockSuperAdminAccount);
        Mockito.when(userRoleService.saveOrGet(UserRoleEnum.SUPER_ADMIN)).thenReturn(mockSuperAdmin);
        Mockito.when(userRoleService.saveOrGet(UserRoleEnum.ADMIN)).thenReturn(mockAdmin);
        Mockito.when(userRoleService.saveOrGet(UserRoleEnum.USER)).thenReturn(mockUser);

//        When
        UserAccount actualSuperAdminAccount = userAccountRepository.saveAndFlush(mockSuperAdminAccount);
        UserRole actualSuperAdmin = userRoleService.saveOrGet(UserRoleEnum.SUPER_ADMIN);
        UserRole actualAdmin = userRoleService.saveOrGet(UserRoleEnum.ADMIN);
        UserRole actualUser = userRoleService.saveOrGet(UserRoleEnum.USER);
        actualSuperAdminAccount.setRoles(List.of(actualSuperAdmin, actualAdmin, actualUser));

//        Then
        assertNotNull(actualSuperAdminAccount);
        assertEquals(mockSuperAdminAccount.getId(), actualSuperAdminAccount.getId());
        assertEquals(mockSuperAdminAccount.getUsername(), actualSuperAdminAccount.getUsername());
        assertEquals(mockSuperAdminAccount.getPassword(), actualSuperAdminAccount.getPassword());
        assertEquals(mockSuperAdminAccount.getRoles(), actualSuperAdminAccount.getRoles());
        assertEquals(mockSuperAdminAccount.getIsEnable(), actualSuperAdminAccount.getIsEnable());
    }

    @Test
    void shouldCreateCustomerAndUserAccountAsRoleUserWhenRegisterUser() {
//        Given
        RegisterRequest mockRegisterRequest = RegisterRequest.builder()
                .name("name")
                .phone("089")
                .username("username")
                .password("password")
                .build();

        UserRole mockRoleUser = UserRole.builder()
                .id("role-user-id")
                .role(UserRoleEnum.USER)
                .build();

        String mockHashedPassword = "hashedPassword";

        UserAccount mockUserAccount = UserAccount.builder()
                .id("user-account-id")
                .username(mockRegisterRequest.getUsername())
                .password(mockHashedPassword)
                .roles(List.of(mockRoleUser))
                .isEnable(true)
                .build();

        NewAccountRequest newAccountRequest = NewAccountRequest.builder()
                .name(mockRegisterRequest.getName())
                .phone(mockRegisterRequest.getPhone())
                .userAccount(mockUserAccount)
                .build();

        Customer customer = Customer.builder()
                .id("customer-id")
                .name(newAccountRequest.getName())
                .phone(newAccountRequest.getPhone())
                .userAccount(mockUserAccount)
                .build();

//        Stubbing config
        Mockito.doNothing().when(validationUtil).validate(mockRegisterRequest);
        Mockito.when(passwordEncoder.encode(mockRegisterRequest.getPassword())).thenReturn(mockHashedPassword);
        Mockito.when(userAccountRepository.saveAndFlush(mockUserAccount)).thenReturn(mockUserAccount);
        Mockito.when(customerService.saveAccount(newAccountRequest)).thenReturn(customer);

//        When
        validationUtil.validate(mockRegisterRequest);
        String actualHashedPassword = passwordEncoder.encode(mockRegisterRequest.getPassword());
        UserAccount actualUserAccount = userAccountRepository.saveAndFlush(mockUserAccount);
        Customer actualCustomer = customerService.saveAccount(newAccountRequest);

//        Then
        assertNotNull(actualHashedPassword);
        assertEquals(mockHashedPassword, actualHashedPassword);

        assertNotNull(actualUserAccount);
        assertEquals(mockUserAccount.getId(), actualUserAccount.getId());
        assertEquals(mockUserAccount.getUsername(), actualUserAccount.getUsername());
        assertEquals(mockUserAccount.getPassword(), actualUserAccount.getPassword());
        assertEquals(mockUserAccount.getRoles(), actualUserAccount.getRoles());
        assertEquals(mockUserAccount.getIsEnable(), actualUserAccount.getIsEnable());

        assertNotNull(actualCustomer);
        assertEquals(customer.getId(), actualCustomer.getId());
        assertEquals(customer.getName(), actualCustomer.getName());
        assertEquals(customer.getPhone(), actualCustomer.getPhone());
        assertEquals(customer.getUserAccount(), actualCustomer.getUserAccount());
    }

    @Test
    void shouldCreateCustomerAndUserAccountHasRoleAdminAndUserWhenRegisterAdmin() {
//        Given
        RegisterRequest mockRegisterRequest = RegisterRequest.builder()
                .name("name")
                .phone("089")
                .username("username")
                .password("password")
                .build();

        UserRole mockRoleAdmin = UserRole.builder()
                .id("role-admin-id")
                .role(UserRoleEnum.ADMIN)
                .build();
        UserRole mockRoleUser = UserRole.builder()
                .id("role-user-id")
                .role(UserRoleEnum.USER)
                .build();

        String mockHashedPassword = "hashedPassword";

        UserAccount mockUserAccount = UserAccount.builder()
                .id("user-account-id")
                .username(mockRegisterRequest.getUsername())
                .password(mockHashedPassword)
                .roles(List.of(mockRoleUser, mockRoleAdmin))
                .isEnable(true)
                .build();

        NewAccountRequest newAccountRequest = NewAccountRequest.builder()
                .name(mockRegisterRequest.getName())
                .phone(mockRegisterRequest.getPhone())
                .userAccount(mockUserAccount)
                .build();

        Customer customer = Customer.builder()
                .id("customer-id")
                .name(newAccountRequest.getName())
                .phone(newAccountRequest.getPhone())
                .userAccount(mockUserAccount)
                .build();

//        Stubbing config
        Mockito.doNothing().when(validationUtil).validate(mockRegisterRequest);
        Mockito.when(passwordEncoder.encode(mockRegisterRequest.getPassword())).thenReturn(mockHashedPassword);
        Mockito.when(userAccountRepository.saveAndFlush(mockUserAccount)).thenReturn(mockUserAccount);
        Mockito.when(customerService.saveAccount(newAccountRequest)).thenReturn(customer);

//        When
        validationUtil.validate(mockRegisterRequest);
        String actualHashedPassword = passwordEncoder.encode(mockRegisterRequest.getPassword());
        UserAccount actualUserAccount = userAccountRepository.saveAndFlush(mockUserAccount);
        Customer actualCustomer = customerService.saveAccount(newAccountRequest);

//        Then
        assertNotNull(actualHashedPassword);
        assertEquals(mockHashedPassword, actualHashedPassword);

        assertNotNull(actualUserAccount);
        assertEquals(mockUserAccount.getId(), actualUserAccount.getId());
        assertEquals(mockUserAccount.getUsername(), actualUserAccount.getUsername());
        assertEquals(mockUserAccount.getPassword(), actualUserAccount.getPassword());
        assertEquals(mockUserAccount.getRoles(), actualUserAccount.getRoles());
        assertEquals(mockUserAccount.getIsEnable(), actualUserAccount.getIsEnable());

        assertNotNull(actualCustomer);
        assertEquals(customer.getId(), actualCustomer.getId());
        assertEquals(customer.getName(), actualCustomer.getName());
        assertEquals(customer.getPhone(), actualCustomer.getPhone());
        assertEquals(customer.getUserAccount(), actualCustomer.getUserAccount());
    }

    @Test
    void shouldReturnLoginResponseWhenLogin() {
        Authentication mockAuthentication = Mockito.mock(Authentication.class);
//        Given
        LoginRequest mockLoginRequest = LoginRequest.builder()
                .username("username")
                .password("password")
                .build();

        List<String> mockUserRoles = List.of(UserRoleEnum.SUPER_ADMIN.name(), UserRoleEnum.ADMIN.name(), UserRoleEnum.USER.name());

        List<UserRole> mockUserRoleList = new ArrayList<>();
        for (String role : mockUserRoles) {
            UserRole userRole = UserRole.builder()
                    .id(role + "-id")
                    .role(UserRoleEnum.valueOf(role))
                    .build();
            mockUserRoleList.add(userRole);
        }

        String mockToken = "mockedToken";

        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(UserAccount.builder()
                .username(mockLoginRequest.getUsername())
                .password(mockLoginRequest.getPassword())
                .roles(mockUserRoleList)
                .isEnable(true)
                .build());

        UserAccount mockUserAccount = (UserAccount) mockAuthentication.getPrincipal();

        LoginResponse mockLoginResponse = LoginResponse.builder()
                .username(mockUserAccount.getUsername())
                .token(mockToken)
                .roles(mockUserRoles)
                .build();

//        Stubbing config
        Mockito.doNothing().when(validationUtil).validate(mockLoginRequest);
        Mockito.when(authenticationManager.authenticate(mockAuthentication)).thenReturn(mockAuthentication);
        Mockito.when(jwtService.generateToken(mockUserAccount)).thenReturn(mockToken);

//        When
        validationUtil.validate(mockLoginRequest);
        Authentication actualAuthentication = authenticationManager.authenticate(mockAuthentication);
        Mockito.when(actualAuthentication.getPrincipal()).thenReturn(mockUserAccount);
        UserAccount actualUserAccount = (UserAccount) actualAuthentication.getPrincipal();
        String actualToken = jwtService.generateToken(mockUserAccount);

        LoginResponse actualLoginResponse = LoginResponse.builder()
                .username(mockUserAccount.getUsername())
                .token(actualToken)
                .roles(actualUserAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

//        Then
        assertNotNull(actualToken);
        assertEquals(mockToken, actualToken);
        assertEquals(mockUserAccount.getId(), actualUserAccount.getId());
        assertEquals(mockLoginResponse.getUsername(), actualLoginResponse.getUsername());
        assertEquals(mockLoginResponse.getToken(), actualLoginResponse.getToken());
        assertEquals(mockLoginResponse.getRoles(), actualLoginResponse.getRoles());
    }
}