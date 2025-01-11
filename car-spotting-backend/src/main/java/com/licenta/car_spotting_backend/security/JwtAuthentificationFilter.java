package com.licenta.car_spotting_backend.security;

import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthentificationFilter extends OncePerRequestFilter {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserRepository userRepository;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException { //aceasta functie preia tokenul care se trimite cu api-ul, si separa usernameul de restul tokenului, il cauta in lista de useri, api il pune in SecurityContextHolder
                                                                                                                                                                //un loc de unde putem preula userul care este logat in prezent

        String token = request.getHeader("Authorization");

        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            try {
                String username = jwtTokenUtil.validateToken(token);
                User user = userRepository.findByUsername(username).orElseThrow( () -> new RuntimeException("User in token not found ! :("));

                SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user));
            } catch (Exception e){

            }
        }

        filterChain.doFilter(request, response);
    }
}
