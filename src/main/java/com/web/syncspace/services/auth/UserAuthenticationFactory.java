package com.web.syncspace.services.auth;

import com.web.syncspace.config.SecurityConfig;
import com.web.syncspace.dto.register.AdminRegisterDTO;
import com.web.syncspace.dto.register.RegisterDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.enums.Role;
import com.web.syncspace.models.auth.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFactory {

    private final SecurityConfig securityConfig;

    private UserAuthentication saveUser(UsersRegisterDTO usersRegisterDTO) {
        return UserAuthentication.builder()
                .email(usersRegisterDTO.getEmail())
                .password(securityConfig.passwordEncoder().encode(usersRegisterDTO.getPassword()))
                .mobileNumber(usersRegisterDTO.getMobileNumber())
                .role(Role.USER)
                .build();
    }

    private UserAuthentication saveAdmin(AdminRegisterDTO adminRegisterDTO) {
        return UserAuthentication.builder()
                .email(adminRegisterDTO.getEmail())
                .password(securityConfig.passwordEncoder().encode(adminRegisterDTO.getPassword()))
                .mobileNumber(adminRegisterDTO.getMobileNumber())
                .role(Role.ADMIN)
                .build();
    }

    public UserAuthentication save(RegisterDTO registerRequest) {
        if (registerRequest instanceof UsersRegisterDTO) {
            return saveUser((UsersRegisterDTO) registerRequest);
        } else if (registerRequest instanceof AdminRegisterDTO) {
            return saveAdmin((AdminRegisterDTO) registerRequest);
        }
        return null;
    }

}
