package com.example.ec.web;

import com.example.ec.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    //@GetMapping("/signin")
    public Authentication login(@RequestBody @Valid LoginDto loginDto) {
        System.out.println("UserController.login: username [" + loginDto.getUsername() + "] password [" + loginDto.getPassword() + "]");
        return userService.signin(loginDto.getUsername(), loginDto.getPassword()) ;
    }
}