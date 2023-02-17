package com.example.ec.service;

import com.example.ec.domain.Role;
import com.example.ec.domain.User;
import com.example.ec.repo.RoleRepository;
import com.example.ec.repo.UserRepository;
import com.example.ec.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    //public Authentication signin(String username, String password) {
    //    System.out.println("UserService.signin: username [" + username + "] password [" + password + "]");
    //    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    //}

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     * @param username
     * @param password
     * @return Optional of the Java Web Token, empty otherwise
     */
    public Optional<String> signin(String username, String password) {
        LOGGER.info("UserService.signin: new user attemptiong to sign in: username [" + username + "] password [" + password + "]");
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            try {
                LOGGER.info("signin: have " + user.get().getRoles().size() + " roles for " + username);
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                LOGGER.info("signin: calling jwtProvider.createToken");
                token = Optional.of(jwtProvider.createToken(username, user.get().getRoles()));
                LOGGER.info("signin: after calling jwtProvider.createToken");
            } catch (AuthenticationException e) {
                LOGGER.info("UserService.signin: Log in failed for user {}", username);
            }
        }
        return token;
    }

    /**
     * Create a new user in the database
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @return Optional of user, empty if the user already exists
     */
    public Optional<User> signup(String username, String password, String firstName, String lastName) {
        LOGGER.info("UserService.signup: new user attemptiong to sign in: username [" + username + "] password [" + password + "]");
        Optional<User> user = Optional.empty();
        if(!userRepository.findByUsername(username).isPresent()) {
            Optional<Role> role = roleRepository.findByRoleName("ROLE_CSR");
            user = Optional.of(userRepository.save(new User(username,
                            passwordEncoder.encode(password),
                            role.get(),
                            firstName,
                            lastName)));
        }
        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
 }