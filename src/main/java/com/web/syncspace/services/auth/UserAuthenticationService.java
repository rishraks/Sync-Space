package com.web.syncspace.services.auth;


import com.web.syncspace.dto.auth.AuthResponseDTO;
import com.web.syncspace.dto.login.LoginDTO;
import com.web.syncspace.dto.register.AdminRegisterDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.dto.register.RegisterResponseDTO;
import jakarta.validation.Valid;

public interface UserAuthenticationService {
    RegisterResponseDTO registerUser(@Valid UsersRegisterDTO usersRegisterDTO);

    RegisterResponseDTO registerAdmin(@Valid AdminRegisterDTO adminRegisterDTO);

    AuthResponseDTO login(@Valid LoginDTO loginDTO);

    void logout(String accessToken, String refreshToken);

    AuthResponseDTO refreshToken(String refreshToken);
}
