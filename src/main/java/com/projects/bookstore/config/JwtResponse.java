package com.projects.bookstore.config;

import com.projects.bookstore.users.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class JwtResponse {
    private String token;

    private final String type = "Bearer";

    private String id;

    private String username;

    private String email;

    private UserRole role;
}
