package com.web.syncspace.exceptions.tokenexceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenException extends AuthenticationException {
    public JwtTokenException(String message) {
        super(message);
    }
}
