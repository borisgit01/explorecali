package com.example.ec.security;

import com.example.ec.domain.Role;
import com.example.ec.service.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility Class for common Java Web Token operations
 */
@Component
public class JwtProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
    private final String ROLES_KEY = "roles";
    private JwtParser parser;
    private String secretKey;
    private long validityInMilliseconds;

    public JwtProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                       @Value("${security.jwt.token.expiration}") long validityInMilliseconds) {
        LOGGER.info("JwtProvider constructor, validityInMilliseconds = " + validityInMilliseconds);
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String username, List<Role> roles) {
        LOGGER.info("createToken - have " + roles.size() + " roles");
        //Add the username to the payload
        Claims claims = Jwts.claims().setSubject(username);
        LOGGER.info("createToken - mark 1");
        for(Role theRole : roles) {
            LOGGER.info("createToken - have authority " + theRole.getAuthority());
        }
        //Convert roles to Spring Security SimpleGrantedAuthority objects.
        //add to Simple Granted Authority objects to claims
        claims.put(ROLES_KEY, roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        LOGGER.info("createToken - mark 2");
        //Build the token
        Date now = new Date();
        LOGGER.info("createToken - building return string");
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Validate the JWT String
     * @param token JWT string
     * @return true if valid, false otherwise
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            LOGGER.error("isValidToken - exception " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the username from token string
     * @param token jwt
     * @return username
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get the roles from the token string
     * @param token jwt
     * @return authorities
     */
    public List<GrantedAuthority> getRoles(String token) {
        List<Map<String, String>> roleClaims = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().get(ROLES_KEY, List.class);
        return roleClaims.stream().map(roleClaim ->
                new SimpleGrantedAuthority(roleClaim.get("authority")))
                .collect(Collectors.toList());
    }
}
