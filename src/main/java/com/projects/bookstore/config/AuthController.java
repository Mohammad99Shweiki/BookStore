package com.projects.bookstore.config;

import com.projects.bookstore.config.jwt.JwtUtils;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserService;
import com.projects.bookstore.users.order.Cart;
import com.projects.bookstore.users.order.OrdersEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationCache authenticationCache;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();

        return ResponseEntity.ok(JwtResponse.builder()
                .id(userDetails.getUserId())
                .token(jwt)
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .role(userDetails.getRole())
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .favoriteGenres(registerRequest.getFavoriteGenres() == null ? new HashSet<>() : registerRequest.getFavoriteGenres())
                .password(encoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .cart(new Cart())
                .orders(new OrdersEntity())
                .wishlist(new HashSet<>())
                .build();

        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
                                         @RequestHeader("Authorization") String authorizationHeader) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            String jwtToken = authorizationHeader.replace("Bearer ", "");

            authenticationCache.invalidateJwt(jwtToken);

            return ResponseEntity.ok("Logged out successfully");
        }

        return ResponseEntity.badRequest().body("No user to logout");
    }
}