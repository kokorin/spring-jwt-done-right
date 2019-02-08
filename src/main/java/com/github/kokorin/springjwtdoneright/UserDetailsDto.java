package com.github.kokorin.springjwtdoneright;

import java.util.Date;
import java.util.List;

public class UserDetailsDto {
    public String username;
    public List<String> authorities;
    public Date expires;

    public UserDetailsDto() {
    }

    public UserDetailsDto(String username, List<String> authorities, Date expires) {
        this.username = username;
        this.authorities = authorities;
        this.expires = expires;
    }
}
