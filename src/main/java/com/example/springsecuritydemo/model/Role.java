package com.example.springsecuritydemo.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    CLIENT(Set.of(Permission.USER_READ, Permission.USER_UPDATE)),
    COACH(Set.of(Permission.USER_READ, Permission.USER_UPDATE)),
    USER(Set.of(Permission.USER_READ, Permission.USER_UPDATE)),
    ADMIN(Set.of(Permission.ADMIN_CREATE, Permission.ADMIN_DELETE, Permission.ADMIN_UPDATE, Permission.ADMIN_READ, Permission.USER_READ,Permission.USER_UPDATE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
