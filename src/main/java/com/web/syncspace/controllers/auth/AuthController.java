package com.web.syncspace.controllers.auth;

import com.web.syncspace.constants.Constants;
import com.web.syncspace.dto.auth.AuthResponseDTO;
import com.web.syncspace.dto.login.LoginDTO;
import com.web.syncspace.dto.login.LoginResponseDTO;
import com.web.syncspace.dto.register.AdminRegisterDTO;
import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.dto.response.ResponseDTO;
import com.web.syncspace.services.auth.UserAuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;

    @PostMapping("/user-register")
    public ResponseEntity<ResponseDTO<RegisterResponseDTO>> registerUser(@Valid @RequestBody UsersRegisterDTO usersRegisterDTO) {
        RegisterResponseDTO registerResponseDTO = userAuthenticationService.registerUser(usersRegisterDTO);
        ResponseDTO<RegisterResponseDTO> responseDTO = ResponseDTO.<RegisterResponseDTO>builder()
                .statusCode(Constants.SUCCESS_CODE)
                .statusMessage(Constants.SUCCESS_MESSAGE)
                .data(registerResponseDTO)
                .additionalData("User registered successfully!!!")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/admin-register")
    public ResponseEntity<ResponseDTO<RegisterResponseDTO>> registerAdmin(@Valid @RequestBody AdminRegisterDTO adminRegisterDTO) {
        RegisterResponseDTO registerResponseDTO = userAuthenticationService.registerAdmin(adminRegisterDTO);
        ResponseDTO<RegisterResponseDTO> responseDTO = ResponseDTO.<RegisterResponseDTO>builder()
                .statusCode(Constants.SUCCESS_CODE)
                .statusMessage(Constants.SUCCESS_MESSAGE)
                .data(registerResponseDTO)
                .additionalData("Admin registered successfully!!!")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse httpServletResponse) {
        AuthResponseDTO authResponseDTO = userAuthenticationService.login(loginDTO);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", authResponseDTO.getRefreshToken())
                .httpOnly(true)
                .sameSite("Strict")
                .secure(true)
                .maxAge(24 * 60 * 60)
                .path("/")
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        LoginResponseDTO loginResponseDTO = authResponseDTO.getLoginResponseDTO();

        ResponseDTO<LoginResponseDTO> responseDTO = ResponseDTO.<LoginResponseDTO>builder()
                .statusCode(Constants.SUCCESS_CODE)
                .statusMessage(Constants.SUCCESS_MESSAGE)
                .data(loginResponseDTO)
                .additionalData("User logged in successfully!!!")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<?>> logout(@RequestHeader(value = "Authorization") String authHeader, @CookieValue(value = "refreshToken") String refreshToken) {
        String accessToken = authHeader.substring("Bearer ".length());
        userAuthenticationService.logout(accessToken, refreshToken);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                .statusCode(Constants.SUCCESS_CODE)
                .statusMessage(Constants.SUCCESS_MESSAGE)
                .data("User logged out successfully!!!")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDTO<LoginResponseDTO>> refreshToken(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse httpServletResponse) {
        AuthResponseDTO authResponseDTO = userAuthenticationService.refreshToken(refreshToken);
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", authResponseDTO.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        LoginResponseDTO loginResponseDTO = authResponseDTO.getLoginResponseDTO();

        ResponseDTO<LoginResponseDTO> responseDTO = ResponseDTO.<LoginResponseDTO>builder()
                .statusCode(Constants.SUCCESS_CODE)
                .statusMessage(Constants.SUCCESS_MESSAGE)
                .data(loginResponseDTO)
                .additionalData("Token refreshed successfully!!!")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


}
