package com.suryadeep.authstarter.controller;

import com.suryadeep.authstarter.dto.request.LoginRequest;
import com.suryadeep.authstarter.dto.request.UserRegisterRequest;
import com.suryadeep.authstarter.dto.response.LoginResponse;
import com.suryadeep.authstarter.dto.response.UserResponse;
import com.suryadeep.authstarter.entity.User;
import com.suryadeep.authstarter.exception.EmailAlreadyExistsException;
import com.suryadeep.authstarter.security.CustomUserDetails;
import com.suryadeep.authstarter.service.AuthenticationService;
import com.suryadeep.authstarter.service.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) {
        try {
            User registerUser = authenticationService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(registerUser));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // AuthenticationManager handles UserNotFound, BadCredentials automatically
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // If authentication is successful, SecurityContext is updated
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(jwtToken));
    }


}

