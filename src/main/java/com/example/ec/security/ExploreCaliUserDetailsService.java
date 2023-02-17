package com.example.ec.security;

import com.example.ec.domain.Role;
import com.example.ec.domain.User;
import com.example.ec.repo.UserRepository;
import com.example.ec.web.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

/**
 * Service to associate user with password and roles setup in the database.
 *
 * Created by Mary Ellen Bowman
 */
@Component
public class ExploreCaliUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExploreCaliUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LOGGER.info("ExploreCaliUserDetailsService.loadUserByUsername - entering");
        User user = userRepository.findByUsername(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with name %s does not exist", s)));
        List<Role> userRolesList = user.getRoles();
        LOGGER.info("ExploreCaliUserDetailsService.loadUserByUsername - have " + userRolesList.size() + " roles");
        return withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Extract username and roles from a validated jwt string
     * @param jwtToken jwt string
     * @return UserDetails if valid, Empty otherwise
     */
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        LOGGER.info("loadUserByJwtToken - jwtToken is [" + jwtToken + "]");
        if(jwtProvider.isValidToken(jwtToken)) {
            LOGGER.info("loadUserByJwtToken - jwtToken is valid");
            LOGGER.info("loadUserByJwtToken - username is [" +  jwtProvider.getUsername(jwtToken) + "]");
            return Optional.of(
                    withUsername(jwtProvider.getUsername(jwtToken))
                            .authorities(jwtProvider.getRoles(jwtToken))
                            .password("") //token does not have password but field may not be empty
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(false)
                            .build());
        }
        LOGGER.info("loadUserByJwtToken - jwtToken is not valid");
        return Optional.empty();
    }

    public Optional<UserDetails> loadUserByJwtTokenAndDatabase(String jwtToken) {
        if(jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(loadUserByUsername(jwtProvider.getUsername(jwtToken)));
        } else {
            return Optional.empty();
        }
    }

    public UserDetails myLoadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("ExploreCaliUserDetailsService.loadUserByUsername - entering");
        User user = userRepository.findByUsername(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with name %s does not exist", s)));
        System.out.println("ExploreCaliUserDetailsService.loadUserByUsername - have user by name " + s);
        List<Role> userRoles = user.getRoles();
        System.out.println("ExploreCaliUserDetailsService.loadUserByUsername - user [" + user.getUsername() + "] has " + userRoles.size() + " roles");
        String[] userRoleNames = new String[userRoles.size()];
        int i = 0;
        for(Role theRole : userRoles) {
            System.out.println(theRole.getRoleName());
            userRoleNames[i] = theRole.getRoleName();
            i++;
        }
        //withUsername(user.getUsername()).authorities();
        //org.springframework.security.core.userdetails.User.withUsername() builder
        return withUsername(user.getUsername())
                .password(user.getPassword())
//                .authorities(user.getRoles())
//                .authorities("ROLE_ADMIN")
                .authorities(userRoleNames)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}