package com.example.springsecuritydemo.model;

public enum Permission {
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_CREATE("admin:update"),
    ADMIN_UPDATE("admin:create"),
    ADMIN_READ("admin:read");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
