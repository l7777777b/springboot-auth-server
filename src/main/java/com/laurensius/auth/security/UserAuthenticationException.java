package com.laurensius.auth.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is throw in case of a not activated user trying to authenticate.
 */
public class UserAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserAuthenticationException(String message) {
        super(message);
    }

    public UserAuthenticationException(String message, Throwable t) {
        super(message, t);
    }
}
