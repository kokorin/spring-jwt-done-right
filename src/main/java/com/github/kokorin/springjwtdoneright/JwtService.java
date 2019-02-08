package com.github.kokorin.springjwtdoneright;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtService {
    private Key key;

    private static final String AUTHORITIES_CLAIM = "authorities";


    @Value("${jwt.key:simple_key_must_be_at_least_80_bits_long}")
    public void setKey(String keyStr) {
        key = Keys.hmacShaKeyFor(keyStr.getBytes());
    }

    public String toJwt(SimpleUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(userDetails.getExpiresAfter()))
                .claim(
                        AUTHORITIES_CLAIM,
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","))
                )
                .signWith(key)
                .compact();
    }

    public SimpleUserDetails fromJwt(String jwt) {
        Claims claims = (Claims) Jwts.parser()
                .setSigningKey(key)
                .parse(jwt)
                .getBody();

        return new SimpleUserDetails(
                claims.getSubject(),
                "N/A",
                Arrays.stream(claims.get(AUTHORITIES_CLAIM, String.class).split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()),
                claims.getExpiration().toInstant()
        );
    }
}
