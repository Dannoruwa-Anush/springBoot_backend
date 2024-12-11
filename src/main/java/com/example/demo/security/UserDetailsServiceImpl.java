package com.example.demo.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //This file implements UserDetailsService that is defined in the springframework.security.core

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElse(null);
        //Load User Roles

        if(user == null) {
            throw new UsernameNotFoundException("User not found with the given username");
        }

        //------ modified----
        Set<GrantedAuthority> authorities = user.getRoles().stream()
        .flatMap(role -> role.getUserPermissions().stream())
        .map(permission -> new SimpleGrantedAuthority(permission.getUserPermissionName()))
        .collect(Collectors.toSet());
        //---------------------


        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(authorities) //modified
            .build();
    }
    //---
}
