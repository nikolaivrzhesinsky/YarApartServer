package com.example.authenticationya.config.securityConfig;

import com.example.authenticationya.entity.Role;
import com.example.authenticationya.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authenticationya.repository.UserRepository;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User didnt exist"));
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(), mapRoleToAuthority(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRoleToAuthority(Set<Role> roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole().toString()))
                .collect(Collectors.toList());
    }
}
