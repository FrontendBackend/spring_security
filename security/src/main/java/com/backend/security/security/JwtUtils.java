package com.backend.security.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jvale.app.jwtSecret}")
    private String jwtSecret;

    @Value("${jvale.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jvale.app.jwtRefreshExpirationMs}")
    private long refreshTokenExpirationMs;

    public String generateJwtToken(Authentication authentication, UUID accessTokenId) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // return Jwts.builder()
        //         .setSubject((userPrincipal.getUsername()))
        //         .setIssuedAt(new Date())
        //         .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        //         .signWith(key(), SignatureAlgorithm.HS256)
        //         .compact();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // ✅ Fuerza incluir "typ": "JWT"
                .claim("username", userPrincipal.getUsername()) // sub
                .claim("id", userPrincipal.getId()) // ID del usuario
                .claim("email", userPrincipal.getEmail()) // Email
                .claim("authorities", userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()) // Roles o permisos
                .setId(accessTokenId.toString()) // jti
                .setIssuedAt(new Date()) // iat
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // exp
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // public String generateRefreshToken(UserDetailsImpl userDetails) {
    //     return Jwts.builder()
    //             .setSubject(userDetails.getUsername())
    //             .setIssuedAt(new Date())
    //             .setExpiration(new Date((new Date()).getTime() + refreshTokenExpirationMs))
    //             .signWith(key(), SignatureAlgorithm.HS256)
    //             .compact();
    // }

    public String generateRefreshToken(UserDetailsImpl userDetails, UUID accessTokenId) {
        UUID refreshTokenId = UUID.randomUUID(); // jti del refresh token

        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // ✅ Fuerza incluir "typ": "JWT"
                .claim("username", userDetails.getUsername()) // sub
                .claim("id", userDetails.getId()) // ID
                .claim("email", userDetails.getEmail()) // email
                .claim("authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()) // roles
                .claim("ati", accessTokenId.toString()) // Access Token ID
                .setId(refreshTokenId.toString()) // jti del refresh token
                .setIssuedAt(new Date()) // iat
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs)) // exp más largo
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }    
    
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
