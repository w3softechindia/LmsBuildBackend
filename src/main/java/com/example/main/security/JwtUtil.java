package com.example.main.security;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.xml.bind.DatatypeConverter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // Default validity

    private byte[] key = DatatypeConverter.parseBase64Binary("pass@123jgyktdyrdjhbjhljnlknlknrtskyrsyrsjyrastjrsjrstrsjtrgphfgkhdkffgkmgitmbhirmyoihmyoinmbgembvkjnhknrthneplbplgtmbmgtbgrtbmgrbriobm");

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("deprecation")
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), rememberMe);
    }

    @SuppressWarnings("deprecation")
    private String doGenerateToken(Map<String, Object> claims, String subject, boolean rememberMe) {
        long validity = rememberMe ? JWT_TOKEN_VALIDITY * 2 : JWT_TOKEN_VALIDITY; // Example: Double the validity if rememberMe is true
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
                .signWith(SignatureAlgorithm.HS512, key).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}