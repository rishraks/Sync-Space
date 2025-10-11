package com.web.syncspace.exceptions.tokenexceptions;

import org.springframework.security.core.AuthenticationException;

public class MalformedTokenException extends AuthenticationException {
    public MalformedTokenException(String message) {
        super(message);
    }
}
