package com.licenta.car_spotting_backend;

import com.licenta.car_spotting_backend.dto.JsonResponse;
import com.licenta.car_spotting_backend.dto.LoginRequestDTO;
import com.licenta.car_spotting_backend.dto.RegisterRequestDTO;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.UserRepository;
import com.licenta.car_spotting_backend.security.JwtTokenUtil;
import com.licenta.car_spotting_backend.services.AuthenticationServices;
import com.licenta.car_spotting_backend.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {
    @Test
    void registerUser_returnsSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        EmailService emailService = mock(EmailService.class);

        AuthenticationServices service = new AuthenticationServices();
        service.setUserRepository(userRepository);
        service.setPasswordEncoder(passwordEncoder);
        service.setJwtTokenUtil(jwtTokenUtil);
        service.setEmailService(emailService);

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("newuser");
        dto.setPassword("password");
        dto.setEmail("email@test.com");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpw");

        JsonResponse response = service.registerUser(dto);

        assertEquals(200, response.getStatus());
        assertEquals("User created succefully", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendWelcomeEmail("email@test.com", "newuser");
    }
    @Test
    void registerUser_returnsUserAlreadyExists() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        EmailService emailService = mock(EmailService.class);

        AuthenticationServices service = new AuthenticationServices();
        service.setUserRepository(userRepository);
        service.setPasswordEncoder(passwordEncoder);
        service.setJwtTokenUtil(jwtTokenUtil);
        service.setEmailService(emailService);

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("newuser");
        dto.setPassword("password");
        dto.setEmail("email@test.com");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedpw");
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.of(new User()));

        JsonResponse response = service.registerUser(dto);

        assertEquals(400, response.getStatus());
        assertEquals("Username is already in use", response.getMessage());
    }

    @Test
    void loginUser_returnsSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        EmailService emailService = mock(EmailService.class);

        AuthenticationServices service = new AuthenticationServices();
        service.setUserRepository(userRepository);
        service.setPasswordEncoder(passwordEncoder);
        service.setJwtTokenUtil(jwtTokenUtil);
        service.setEmailService(emailService);

        User mockUser = new User();
        mockUser.setUsername("USER");
        mockUser.setPassword("<PASSWORD>");

        when(userRepository.findByUsername("USER")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenUtil.generateToken(anyString())).thenReturn("token");

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("USER");
        dto.setPassword("<PASSWORD>");

        JsonResponse response = service.loginUser(dto);

        assertEquals(200, response.getStatus());
        assertEquals("token", response.getMessage());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }
    @Test
    void loginUser_returnsInvalidPassword() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
        EmailService emailService = mock(EmailService.class);

        AuthenticationServices service = new AuthenticationServices();
        service.setUserRepository(userRepository);
        service.setPasswordEncoder(passwordEncoder);
        service.setJwtTokenUtil(jwtTokenUtil);
        service.setEmailService(emailService);

        User mockUser = new User();
        mockUser.setUsername("USER");
        mockUser.setPassword("<PASSWORD>");

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("USER");
        dto.setPassword("<PASSWORD_WRONG>");

        when(userRepository.findByUsername("USER")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(mockUser.getPassword(), dto.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(anyString())).thenReturn("token");



        JsonResponse response = service.loginUser(dto);

        assertEquals(400, response.getStatus());
        assertEquals("Invalid password", response.getMessage());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }
}
