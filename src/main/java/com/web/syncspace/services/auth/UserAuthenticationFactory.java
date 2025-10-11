package com.web.syncspace.services.auth;

import com.web.syncspace.config.SecurityConfig;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.enums.Role;
import com.web.syncspace.models.auth.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFactory {

    private final SecurityConfig securityConfig;

    public UserAuthentication saveUser(UsersRegisterDTO usersRegisterDTO) {
        return UserAuthentication.builder()
                .email(usersRegisterDTO.getEmail())
                .password(securityConfig.passwordEncoder().encode(usersRegisterDTO.getPassword()))
                .mobileNumber(usersRegisterDTO.getMobileNumber())
                .role(Role.USER)
                .build();
    }
}
