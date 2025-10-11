package com.web.syncspace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.syncspace.constants.Constants;
import com.web.syncspace.dto.response.ErrorResponseDTO;
import com.web.syncspace.dto.response.ResponseDTO;
import com.web.syncspace.exceptions.tokenexceptions.InvalidTokenException;
import com.web.syncspace.exceptions.tokenexceptions.MalformedTokenException;
import com.web.syncspace.exceptions.tokenexceptions.TokenBlacklistedException;
import com.web.syncspace.exceptions.tokenexceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String message = switch (authException) {
            case InvalidTokenException ex -> ex.getMessage();
            case MalformedTokenException ex -> ex.getMessage();
            case TokenBlacklistedException ex -> ex.getMessage();
            case TokenExpiredException ex -> ex.getMessage();
            default -> "Authentication failed " + authException.getMessage();
        };

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiUri(request.getRequestURI())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .errorMessage(message)
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ResponseDTO<ErrorResponseDTO> responseDTO = ResponseDTO.<ErrorResponseDTO>builder()
                .statusCode(Constants.UNAUTHORIZED_CODE)
                .statusMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .data(errorResponseDTO)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }
}
