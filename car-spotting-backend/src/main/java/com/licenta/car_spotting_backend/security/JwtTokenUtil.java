package com.licenta.car_spotting_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "your-very-secure-secret-key-for-jwt-signing";
    private static final long EXPIRATION_TIME = 86400000;
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String username){ //generam un token care v-a fi trasmis atunci cand apelam api-ul auth/login de forma Authorzation : "Bearer Username" , acest token este criptat folosind HS256
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME)).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String validateToken(String token){ //aceasta functie preia token, si decripteaza continutul si returneaza username-ul
        try{
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            return claims.getSubject();
        }catch (JwtException e){
            throw new RuntimeException("Invalid or exired login token");
        }
    }
}
