package com.programandoenjava.iambackend.user.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserDto(Long id, String username, String password, String email,
                      Collection<? extends GrantedAuthority> role) {
}
