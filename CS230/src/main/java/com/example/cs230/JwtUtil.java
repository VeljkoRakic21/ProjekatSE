package com.example.cs230;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class JwtUtil {
    private static final String SECRET_KEY = "i9gaeto0zkiC94osBljQxKMME7lp3HFzHEgyfQlUOtY="; // Zamenite sa generisanim ključem

    private static Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dan
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
