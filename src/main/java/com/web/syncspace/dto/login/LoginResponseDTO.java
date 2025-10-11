package com.web.syncspace.dto.login;

import com.web.syncspace.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String accessToken;

    private long expiresIn;

    @Builder.Default
    private String tokenType = "JWT";

    private List<String> role;

    private String message;

}
