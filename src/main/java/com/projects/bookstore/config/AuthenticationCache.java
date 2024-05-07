package com.projects.bookstore.config;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AuthenticationCache {
    private Set<String> invalidJwts = new HashSet<>();

    public void invalidateJwt(String jwt) {
        invalidJwts.add(jwt);
    }

    public boolean isValidJwt(String jwt) {
        return !invalidJwts.contains(jwt);
    }
}
