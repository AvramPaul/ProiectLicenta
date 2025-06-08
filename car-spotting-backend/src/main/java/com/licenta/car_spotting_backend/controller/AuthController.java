package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.dto.JsonResponse;
import com.licenta.car_spotting_backend.dto.LoginRequestDTO;
import com.licenta.car_spotting_backend.dto.RegisterRequestDTO;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.UserRepository;
import com.licenta.car_spotting_backend.security.JwtTokenUtil;
import com.licenta.car_spotting_backend.services.AuthenticationServices;
import com.licenta.car_spotting_backend.services.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    EmailService emailService;
    @Autowired
    AuthenticationServices authenticationServices;

    @PostMapping("/register")
    public ResponseEntity<JsonResponse> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        JsonResponse response = authenticationServices.registerUser(registerRequestDTO);
        if(response.status == 400)
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public  ResponseEntity<JsonResponse> login(@RequestBody LoginRequestDTO loginRequestDTO){
        JsonResponse response = authenticationServices.loginUser(loginRequestDTO);
        if(response.status == 400)
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok().body(response);
    }

}
