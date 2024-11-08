package ojosama.talkak.auth.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import ojosama.talkak.auth.config.JwtProperties;

public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpireIn;
    private final Long refreshTokenExpireIn;

    public JwtUtil(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes());
        this.accessTokenExpireIn = jwtProperties.accessTokenExpireIn();
        this.refreshTokenExpireIn = jwtProperties.refreshTokenExpireIn();
    }

    public String generateAccessToken(Long id, String email, String username) {
        long current = System.currentTimeMillis();
        return Jwts.builder()
            .subject(id.toString())
            .claim("email", email)
            .claim("username", username)
            .issuedAt(new Date(current))
            .expiration(new Date(current + accessTokenExpireIn))
            .signWith(secretKey)
            .compact();
    }

    public String generateRefreshToken() {
        long current = System.currentTimeMillis();
        return Jwts.builder()
            .issuedAt(new Date(current))
            .expiration(new Date(current + refreshTokenExpireIn))
            .signWith(secretKey)
            .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException ignored) {
        }
        return false;
    }

    public Long getIdFromToken(String token) {
        return Long.valueOf(Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject());
    }
}
