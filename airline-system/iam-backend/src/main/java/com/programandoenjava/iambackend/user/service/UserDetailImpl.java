package com.programandoenjava.iambackend.user.service;

import com.programandoenjava.iambackend.auth.entity.Role;
import com.programandoenjava.iambackend.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.programandoenjava.iambackend.user.entity.User userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles().stream()
                        .map(Role::getName) // "ADMIN" -> ROLE_ADMIN
                        .toArray(String[]::new))
                .build();
    }
}
