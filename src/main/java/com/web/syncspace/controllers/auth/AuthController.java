package com.web.syncspace.controllers.auth;

import com.web.syncspace.constants.Constants;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.dto.response.ResponseDTO;
import com.web.syncspace.services.auth.UserAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
