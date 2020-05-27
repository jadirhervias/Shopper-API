package com.shopper.shopperapi.utils.security;

public enum ApplicationUserPermission {
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    CATALOGS_READ("catalogs:read"),
    CATALOGS_WRITE("catalogs:write"),
    CATEGORIES_READ("categories:read"),
    CATEGORIES_WRITE("categories:write"),
    PRODUCTS_READ("products:read"),
    PRODUCTS_WRITE("products:write"),
    SHOPS_READ("shops:read"),
    SHOPS_WRITE("shops:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}