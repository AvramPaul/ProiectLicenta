package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.dto.LoginRequestDTO;
import com.licenta.car_spotting_backend.dto.RegisterRequestDTO;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.UserRepository;
import com.licenta.car_spotting_backend.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Username alreay exists!");
        }
        User user = new User(
                registerRequestDTO.getUsername(),
                passwordEncoder.encode(registerRequestDTO.getPassword()),
                registerRequestDTO.getEmail()
        );
        userRepository.save(user);
        return ResponseEntity.ok("User registered succesfully");
    }

    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO){
        User user = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow( () -> new RuntimeException("User not found") );

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            return ResponseEntity.badRequest().body("Invalid password!");
        }

        String token = jwtTokenUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }

    public String logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token.isEmpty())
        {
            System.out.println("Erai deja delog boule");
        }else{
            request.setAttribute("Authorization", " ");
        }
        return "/auth/login";
    }
}
