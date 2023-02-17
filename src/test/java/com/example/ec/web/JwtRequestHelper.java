package com.example.ec.web;

import com.example.ec.domain.Role;
import com.example.ec.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class JwtRequestHelper {
    @Autowired
    private JwtProvider jwtProvider;

    public HttpHeaders withRole(String roleName) {
        HttpHeaders headers = new HttpHeaders();
        Role r = new Role();
        r.setRoleName(roleName);
        String token = jwtProvider.createToken("anonumous", Arrays.asList(r));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}
