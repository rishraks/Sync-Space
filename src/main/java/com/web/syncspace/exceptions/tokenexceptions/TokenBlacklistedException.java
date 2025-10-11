package com.web.syncspace.exceptions.tokenexceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenBlacklistedException extends AuthenticationException {
    public TokenBlacklistedException(String message) {
        super(message);
    }
}
