package com.cnsbd.pms.auth.controller;

import com.cnsbd.pms.auth.dto.LoginRequest;
import com.cnsbd.pms.auth.dto.LoginResponse;
import com.cnsbd.pms.auth.dto.RegRequest;
import com.cnsbd.pms.exception.UsernameNotAvailableException;
import com.cnsbd.pms.pmuser.entity.PmUser;
import com.cnsbd.pms.pmuser.repository.PmUserRepository;
import com.cnsbd.pms.pmuser.service.PmUserDetailsService;
import com.cnsbd.pms.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "authentication related api(s)")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PmUserDetailsService userDetailsService;
    private final PmUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Register for a new account",
            description = "register a new user to the system")
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody RegRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameNotAvailableException("Username is not available!");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        PmUser pmUser = modelMapper.map(request, PmUser.class);
        userRepository.save(pmUser);
        return "Registration Successful";
    }

    @Operation(
            summary = "Login",
            description = "authenticate and generate a JWT token for the user")
    @PostMapping("/login")
    public LoginResponse loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        PmUser pmUser = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();

        return new LoginResponse(
                pmUser.getId(),
                token,
                pmUser.getUsername(),
                pmUser.getEmail(),
                pmUser.getFullName());
    }
}
