package com.github.kokorin.springjwtdoneright;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Date;
import java.util.stream.Collectors;

@RestController("/greet")
public class GreetController {
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public UserDetailsDto greet(@AuthenticationPrincipal Principal principal) {
        SimpleUserDetails userDetails = (SimpleUserDetails) principal;

        return new UserDetailsDto(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()),
                Date.from(userDetails.getExpiresAfter())
        );
    }
}
