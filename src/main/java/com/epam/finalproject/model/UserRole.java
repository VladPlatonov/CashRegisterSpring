package com.epam.finalproject.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    USER,
    CASHIER,
    SENIOR_CASHIER,
    MERCHANT,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
