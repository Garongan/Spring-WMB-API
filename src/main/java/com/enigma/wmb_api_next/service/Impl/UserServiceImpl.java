package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.entity.UserAccount;
import com.enigma.wmb_api_next.repository.UserAccountRepository;
import com.enigma.wmb_api_next.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;
    @Transactional(rollbackFor = Exception.class)

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(StatusMessage.USERNAME_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount getByUserId(String id) {
        return userAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount getByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userAccountRepository.findByUsername(authentication.getPrincipal().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.NOT_FOUND));
    }
}
