package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import com.enigma.wmb_api_next.entity.UserAccount;
import com.enigma.wmb_api_next.entity.UserRole;
import com.enigma.wmb_api_next.repository.CustomerRepository;
import com.enigma.wmb_api_next.repository.UserAccountRepository;
import com.enigma.wmb_api_next.repository.UserRoleRepository;
import com.enigma.wmb_api_next.service.CustomerService;
import com.enigma.wmb_api_next.service.JwtService;
import com.enigma.wmb_api_next.service.UserRoleService;
import com.enigma.wmb_api_next.specification.CustomerSpecification;
import com.enigma.wmb_api_next.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerSpecification specification;
    @Mock
    private CustomerService customerService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Value("${wmb_api_next.jwt.secret}")
    private String JWT_SECRET;
    @Value("${wmb_api_next.jwt.issuer}")
    private String JWT_ISSUER;
    @Value("${wmb_api_next.jwt.expiration}")
    private long JWT_EXPIRATION;
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
        userRoleService = new UserRoleServiceImpl(userRoleRepository);
        customerService = new CustomerServiceImpl(customerRepository, specification, validationUtil);
        jwtService = new JwtServiceImpl(JWT_SECRET, JWT_ISSUER, JWT_EXPIRATION);
    }

    @Test
    void shouldFindSuperAdminWhenInit() {
//        Given
        UserRole superAdmin = UserRole.builder()
                .role(UserRoleEnum.SUPER_ADMIN)
                .build();
        UserRole admin = UserRole.builder()
                .role(UserRoleEnum.ADMIN)
                .build();
        UserRole user = UserRole.builder()
                .role(UserRoleEnum.USER)
                .build();

        UserAccount superAdminAccount = UserAccount.builder()
                .username(superAdminUsername)
                .password(superAdminPassword)
                .roles(List.of(superAdmin, admin, user))
                .build();


//        Stubbing config
        Mockito.when(userAccountRepository.findByUsername(superAdminUsername)).thenReturn(Optional.of(superAdminAccount));

//        When
        Optional<UserAccount> actualSuperAdminAccount = userAccountRepository.findByUsername(superAdminUsername);

//        Then
        assertNotNull(actualSuperAdminAccount);
        actualSuperAdminAccount.ifPresent(userAccount -> {
            assertEquals(superAdminAccount.getUsername(), userAccount.getUsername());
            assertEquals(superAdminAccount.getPassword(), userAccount.getPassword());
            assertEquals(superAdminAccount.getRoles(), userAccount.getRoles());
            assertEquals(superAdminAccount.getIsEnable(), userAccount.getIsEnable());
        });
    }

    @Test
    void shouldCreateSuperAdminWhenInit(){
//        Given
        UserRole superAdmin = UserRole.builder()
                .role(UserRoleEnum.SUPER_ADMIN)
                .build();
        UserRole admin = UserRole.builder()
                .role(UserRoleEnum.ADMIN)
                .build();
        UserRole user = UserRole.builder()
                .role(UserRoleEnum.USER)
                .build();

        UserAccount superAdminAccount = UserAccount.builder()
                .username(superAdminUsername)
                .password(superAdminPassword)
                .roles(List.of(superAdmin, admin, user))
                .build();


//        Stubbing config
        Mockito.when(userAccountRepository.saveAndFlush(superAdminAccount)).thenReturn(superAdminAccount);
        Mockito.when(userRoleService.saveOrGet(UserRoleEnum.SUPER_ADMIN)).thenReturn(superAdmin);
        Mockito.when(userRoleService.saveOrGet(UserRoleEnum.ADMIN)).thenReturn(admin);
        Mockito.when(userRoleService.saveOrGet(UserRoleEnum.USER)).thenReturn(user);

//        When
        UserRole actualSuperAdmin = userRoleService.saveOrGet(UserRoleEnum.SUPER_ADMIN);
        UserRole actualAdmin = userRoleService.saveOrGet(UserRoleEnum.ADMIN);
        UserRole actualUser = userRoleService.saveOrGet(UserRoleEnum.USER);
        UserAccount actualSuperAdminAccount = userAccountRepository.saveAndFlush(superAdminAccount);

//        Then
        assertNotNull(actualSuperAdmin);
        assertNotNull(actualAdmin);
        assertNotNull(actualUser);
        assertEquals(actualSuperAdmin.getRole(), superAdmin.getRole());
        assertEquals(actualAdmin.getRole(), admin.getRole());
        assertEquals(actualUser.getRole(), user.getRole());

        assertNotNull(actualSuperAdminAccount);
        assertEquals(superAdminAccount.getUsername(), actualSuperAdminAccount.getUsername());
        assertEquals(superAdminAccount.getPassword(), actualSuperAdminAccount.getPassword());
        assertEquals(superAdminAccount.getRoles(), actualSuperAdminAccount.getRoles());
        assertEquals(superAdminAccount.getIsEnable(), actualSuperAdminAccount.getIsEnable());
    }

    @Test
    void registerUser() {
    }

    @Test
    void registerAdmin() {
    }

    @Test
    void login() {
    }
}