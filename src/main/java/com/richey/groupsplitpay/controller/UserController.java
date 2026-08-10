package com.richey.groupsplitpay.controller;

import com.richey.groupsplitpay.dto.AuthResponse;
import com.richey.groupsplitpay.dto.LoginRequest;
import com.richey.groupsplitpay.dto.RegisterRequest;
import com.richey.groupsplitpay.dto.UserResponse;
import com.richey.groupsplitpay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request){
        System.out.println("Registering User");
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request){
        System.out.println(request);
        return ResponseEntity.ok(userService.login(request));
    }
}
