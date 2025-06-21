package com.licenta.car_spotting_backend.services;

import com.licenta.car_spotting_backend.dto.JsonResponse;
import com.licenta.car_spotting_backend.dto.LoginRequestDTO;
import com.licenta.car_spotting_backend.dto.RegisterRequestDTO;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.UserRepository;
import com.licenta.car_spotting_backend.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    EmailService emailService;


    public JsonResponse registerUser(RegisterRequestDTO registerRequestDTO) {


        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            return JsonResponse.create(400, "Username is already in use");
        }
        User user = new User(
                registerRequestDTO.getUsername(),
                passwordEncoder.encode(registerRequestDTO.getPassword()),
                registerRequestDTO.getEmail()
        );
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());

        return JsonResponse.create(200, "User created succefully");
    }

    public JsonResponse loginUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow( () -> new RuntimeException("User not found") );

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            return JsonResponse.create(400, "Invalid password");
        }

        String token = jwtTokenUtil.generateToken(user.getUsername());
        return JsonResponse.create(200, token);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }


}
