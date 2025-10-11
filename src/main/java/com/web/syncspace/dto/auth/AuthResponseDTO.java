package com.web.syncspace.dto.auth;

import com.web.syncspace.dto.login.LoginResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private LoginResponseDTO loginResponseDTO;

    private String refreshToken;

}
