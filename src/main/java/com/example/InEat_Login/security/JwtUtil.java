package com.example.InEat_Login.security;

import com.example.InEat_Login.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")        // Base64 encoded secret from application.properties
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}") // Expiration in milliseconds
    private long jwtExpirationMs;

    private SecretKey key;             // Strong key object for signing

    @PostConstruct
    public void init() {
        // Make sure the secret is at least 512 bits (64 bytes) when decoded
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate a JWT for a user
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId())) // User ID as subject
                .claim("email", user.getEmail())         // Custom claim
                .claim("role", user.getRole().name())    // Custom claim
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512) // Secure HS512 signature
                .compact();
    }

    // Parse claims from token
    public Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    // Validate the token (signature & expiration)
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false; // expired, invalid, etc.
        }
    }

    // Extract user ID from token
    public Long getUserId(String token) {
        Claims claims = parseClaims(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
