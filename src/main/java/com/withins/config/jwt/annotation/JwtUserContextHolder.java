package com.withins.config.jwt.annotation;

import com.withins.config.jwt.CustomJwtToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUserContextHolder {

    public static CustomJwtToken getJwtToken() {
        return (CustomJwtToken) SecurityContextHolder.getContext().getAuthentication();
    }
}
