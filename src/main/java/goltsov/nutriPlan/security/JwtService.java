package goltsov.nutriPlan.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret.access}")
    private String accessSecret;

    @Value("${jwt.secret.refresh}")
    private String refreshSecret;

    @Value("${jwt.expiration.access:3600000}")
    private Long accessExpiration;

    @Value("${jwt.expiration.refresh:604800000}")
    private Long refreshExpiration;

    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    public void init() {
        this.accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
        log.info("JWT Service initialized");
    }

    private String generateToken(String username, SecretKey key, long expiration) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateAccessToken(String username) {
        return generateToken(username, accessKey, accessExpiration);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshKey, refreshExpiration);
    }

    public String generateAccessTokenWithClaims(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .claim("roles", roles)
                .signWith(accessKey)
                .compact();
    }


    private Claims extractAllClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token, accessKey).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token, accessKey).getExpiration();
    }

    public List<String> extractRoles(String token) {
        return extractAllClaims(token, accessKey).get("roles", List.class);
    }



    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            log.warn("Expired checking failed: {}", e.getMessage());
            return true;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            log.warn("Validate failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            extractAllClaims(token, refreshKey);
            return true;
        } catch (Exception e) {
            log.warn("Refresh validate failed: {}", e.getMessage());
            return false;
        }
    }


    public String refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        } else {
            String username = extractUsername(refreshToken);
            return generateAccessToken(username);
        }
    }
}
