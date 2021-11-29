package com.example.springsecuritydemo.model;

public enum Permission {
    READ("read"),
    USER_UPDATE("user:update"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_CREATE("admin:update"),
    ADMIN_UPDATE("admin:create");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
