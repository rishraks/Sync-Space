package com.web.syncspace.services.auth;

import com.web.syncspace.dto.auth.AuthResponseDTO;
import com.web.syncspace.dto.login.LoginDTO;
import com.web.syncspace.dto.login.LoginResponseDTO;
import com.web.syncspace.dto.register.AdminRegisterDTO;
import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.exceptions.others.BadCredentialException;
import com.web.syncspace.exceptions.others.DuplicateResourceException;
import com.web.syncspace.repositories.auth.UserAuthenticationRepository;
import com.web.syncspace.security.jwt.JwtBlacklistService;
import com.web.syncspace.security.jwt.JwtUtil;
import com.web.syncspace.security.userdetaiils.CustomUserDetails;
import com.web.syncspace.security.userdetaiils.CustomUserDetailsServiceImpl;
import com.web.syncspace.services.authorites.admin.AdminService;
import com.web.syncspace.services.authorites.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthenticationImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UsersService usersService;
    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;

    private void validateUsers(String email, String mobileNumber) {
        if (userAuthenticationRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("User with email " + email + " already exists.");
        }
        if (userAuthenticationRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new DuplicateResourceException("User with mobile number " + mobileNumber + " already exists.");
        }
    }

    @Override
    public RegisterResponseDTO registerUser(UsersRegisterDTO usersRegisterDTO) {
        validateUsers(usersRegisterDTO.getEmail(), usersRegisterDTO.getMobileNumber());
        return usersService.registerUser(usersRegisterDTO);
    }

    @Override
    public RegisterResponseDTO registerAdmin(AdminRegisterDTO adminRegisterDTO) {
        validateUsers(adminRegisterDTO.getEmail(), adminRegisterDTO.getMobileNumber());
        return adminService.registerAdmin(adminRegisterDTO);
    }

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (Exception e) {
            throw new BadCredentialException("Invalid email or password!!!");
        }
        final CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsServiceImpl.loadUserByUsername(loginDTO.getEmail());
        final String accessToken = jwtUtil.generateAccessToken(customUserDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(customUserDetails);
        final long expiresIn = jwtUtil.extractAccessTokenExpirationDate(accessToken).toInstant().getEpochSecond();
        final List<String> role = jwtUtil.extractRole(accessToken);
        return AuthResponseDTO.builder()
                .loginResponseDTO(LoginResponseDTO.builder()
                        .accessToken(accessToken)
                        .expiresIn(expiresIn)
                        .tokenType("JWT")
                        .role(role)
                        .message("Don't share your token with anyone!!")
                        .build())
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        long accessTokenExpirationMs = jwtUtil.extractAccessTokenExpirationDate(accessToken).getTime();
        long refreshExpirationMs = jwtUtil.extractRefreshTokenExpirationDate(refreshToken).getTime();
        jwtBlacklistService.blacklistToken(accessToken, refreshToken, accessTokenExpirationMs, refreshExpirationMs);
    }


}
