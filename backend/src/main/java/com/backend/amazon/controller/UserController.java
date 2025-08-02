package com.backend.amazon.controller;

import com.backend.amazon.dto.OTPRequest;
import com.backend.amazon.dto.OTPResponse;
import com.backend.amazon.dto.OTPVerificationRequest;
import com.backend.amazon.dto.UserRequest;
import com.backend.amazon.dto.UserResponse;
import com.backend.amazon.service.OTPService;
import com.backend.amazon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private UserService userService;

   
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

   

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<UserResponse> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        Optional<UserResponse> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // OTP endpoints
    @PostMapping("/generate-otp")
    public ResponseEntity<OTPResponse> generateOTP(@RequestBody OTPRequest request) {
        OTPResponse response = otpService.generateOTP(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OTPResponse> verifyOTP(@RequestBody OTPVerificationRequest request) {
        OTPResponse response = otpService.verifyOTP(request);
        return ResponseEntity.ok(response);
    }

}
