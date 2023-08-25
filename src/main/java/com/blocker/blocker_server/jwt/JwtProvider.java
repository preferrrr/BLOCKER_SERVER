package com.blocker.blocker_server.jwt;

import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.InvalidRefreshTokenException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {


    @Value("${jwt.secret}")
    private String secretKey;
    private final long ACCESS_TOKEN_VALID_TIME = 2 * 30 * 60 * 1000L;   // 60분
    private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 14 * 1000L;   // 2주


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String value) {
        Claims claims = Jwts.claims();
        claims.put("value", value);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null)
            return request.getHeader("Authorization").substring(7);

        return null;
    }


    public Authentication getAuthentication(String token) {
        User user = User.builder()
                .email(getEmail(token))
                .roles(getRoles(token))
                .build();

        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }


    public String getEmail(String token) {
        return getClaimsFromToken(token).getBody().getSubject();
    }

    public List<String> getRoles(String token) {
        return (ArrayList<String>) getClaimsFromToken(token).getBody().get("roles");
    }

    public boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = getClaimsFromToken(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new JwtException("[expired jwt] token : " + token);
        } catch (SecurityException e) {
            throw new JwtException("[wrong signature] token : " + token);
        } catch (MalformedJwtException e) {
            throw new JwtException("[invalid jwt] token : " + token);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("[unsupported JWT] token : " + token);
        }
    }

    public Jws<Claims> getClaimsFromToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }


    public String getRefreshTokenValue(String token) {
        Jws<Claims> claims;

        try {
            claims = getClaimsFromToken(token);
        } catch (ExpiredJwtException e) {
            throw new InvalidRefreshTokenException("[expired jwt] token : " + token);
        } catch (SecurityException e) {
            throw new InvalidRefreshTokenException("[wrong signature] token : " + token);
        } catch (MalformedJwtException e) {
            throw new InvalidRefreshTokenException("[invalid refresh token] : " + token);
        } catch (UnsupportedJwtException e) {
            throw new InvalidRefreshTokenException("[unsupported JWT] token : " + token);
        }

        return (String) claims.getBody().get("value");

    }

}