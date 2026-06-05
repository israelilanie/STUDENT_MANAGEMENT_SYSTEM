package com.israel.studentmanagementsystem.security;

import com.israel.studentmanagementsystem.dto.response.UserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECURE_KEY;

    @Value("${jwt.expiration-ms}")
    private long DURATION;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECURE_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, String role){
        return Jwts.builder()
                .subject(username)
                .claim("role",role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + DURATION))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    //Extract Role
    public String extractRole(String token){
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token){
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
