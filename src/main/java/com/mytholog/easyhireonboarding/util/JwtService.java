package com.mytholog.easyhireonboarding.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {

    //@Value("${jwt.secret}")
    private static String SECRET = "v5hX5kZ3OCzUqfJ8tKwLUnN3W5oBXl3hGyEzBfO4rjY=";

    public String generateToken(String email, String name) {
        return Jwts.builder()
                .setSubject(email)
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    public Claims validate(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .build()
                //.parseClaimsJws(token)
                .parseSignedClaims(token)
                .getPayload();
    }
}
