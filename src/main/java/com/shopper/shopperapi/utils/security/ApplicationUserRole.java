package com.shopper.shopperapi.utils.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.shopper.shopperapi.utils.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    CUSTOMER(Sets.newHashSet(
            USERS_WRITE, SHOPS_READ, CATALOGS_READ, CATEGORIES_READ, SUBCATEGORIES_READ,
            PRODUCTS_READ, ORDERS_WRITE, ORDERS_READ
    )),
    SHOPPER(Sets.newHashSet(
            USERS_READ, USERS_WRITE, SHOPS_READ, CATALOGS_READ, CATEGORIES_READ, SUBCATEGORIES_READ,
            PRODUCTS_READ, ORDERS_READ
    )),
    ADMIN(Sets.newHashSet(
            USERS_WRITE, USERS_READ,
            CATALOGS_WRITE, CATALOGS_READ,
            CATEGORIES_WRITE, CATEGORIES_READ,
            SUBCATEGORIES_WRITE, SUBCATEGORIES_READ,
            SHOPS_WRITE, SHOPS_READ,
            PRODUCTS_WRITE, PRODUCTS_READ,
            ORDERS_WRITE, ORDERS_READ
    ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions  = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
