package com.example.ec.security;

import com.example.ec.domain.Role;
import com.example.ec.domain.User;
import com.example.ec.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.security.core.userdetails.User.withUsername;

/**
 * Service to associate user with password and roles setup in the database.
 *
 * Created by Mary Ellen Bowman
 */
@Component
public class ExploreCaliUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
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