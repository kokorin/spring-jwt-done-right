package com.github.kokorin.springjwtdoneright;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

/**
 * Simplest implementation.
 * In real code you would use repository to retrieve user from DB.
 */
public class SimpleUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String role = username.contains("admin") ? "ROLE_ADMIN" : "ROLE_USER";

        return new SimpleUserDetails(
                username,
                username,
                Collections.singletonList(new SimpleGrantedAuthority(role)),
                Instant.now().plus(1, ChronoUnit.HOURS)
        );
    }
}
