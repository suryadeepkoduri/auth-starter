package com.suryadeep.authstarter.service;

import com.suryadeep.authstarter.dto.request.UserRegisterRequest;
import com.suryadeep.authstarter.entity.User;
import com.suryadeep.authstarter.exception.EmailAlreadyExistsException;
import com.suryadeep.authstarter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegisterRequest registerRequest) throws EmailAlreadyExistsException {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        return userRepository.save(user);
    }
}
