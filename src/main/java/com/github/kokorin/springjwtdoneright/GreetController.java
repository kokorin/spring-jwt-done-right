package com.github.kokorin.springjwtdoneright;

import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.stream.Collectors;

@RestController
public class GreetController {
    @RequestMapping(method = RequestMethod.GET, path = "/greet", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDetailsDto greet(@AuthenticationPrincipal SimpleUserDetails userDetails) {
        return new UserDetailsDto(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                Date.from(userDetails.getExpiresAfter())
        );
    }
}
