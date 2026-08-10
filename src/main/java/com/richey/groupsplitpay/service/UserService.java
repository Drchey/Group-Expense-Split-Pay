package com.richey.groupsplitpay.service;

import com.richey.groupsplitpay.dto.AuthResponse;
import com.richey.groupsplitpay.dto.LoginRequest;
import com.richey.groupsplitpay.dto.RegisterRequest;
import com.richey.groupsplitpay.dto.UserResponse;
import com.richey.groupsplitpay.model.Role;
import com.richey.groupsplitpay.model.User;
import com.richey.groupsplitpay.repo.UserRepo;
import com.richey.groupsplitpay.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    // Register New User
    public UserResponse register(RegisterRequest request){

        // Check if user exists
        if(userRepo.existsByUsername(request.username())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Already in Exists");
        }

        if(userRepo.existsByUsername(request.username())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "UserName Already Exists");
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepo.save(user);

        return new UserResponse(
                request.username(),
                savedUser.getRole().name()
        );
    }


    public AuthResponse login(LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        User user = userRepo.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token);

    }

}
