package com.shopper.shopperapi.auth;

import com.shopper.shopperapi.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static com.shopper.shopperapi.utils.security.ApplicationUserRole.*;

public class ApplicationUser implements UserDetails {

    // email
    private final String username;
    private final String password;
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String address;
//    private final boolean active;
    private final Set<? extends GrantedAuthority> grantedAuthorities;

//    private final boolean isAccountNonExpired;
//    private final boolean isAccountNonLocked;
//    private final boolean isCredentialsNonExpired;
//    private final boolean isEnabled;

//    public ApplicationUser(String username,
//                           String password,
//                           Set<? extends GrantedAuthority> grantedAuthorities,
//                           boolean isAccountNonExpired,
//                           boolean isAccountNonLocked,
//                           boolean isCredentialsNonExpired,
//                           boolean isEnabled
//    ) {
//        this.username = username;
//        this.password = password;
//        this.grantedAuthorities = grantedAuthorities;
//        this.isAccountNonExpired = isAccountNonExpired;
//        this.isAccountNonLocked = isAccountNonLocked;
//        this.isCredentialsNonExpired = isCredentialsNonExpired;
//        this.isEnabled = isEnabled;
//    }

    public ApplicationUser(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.grantedAuthorities = user.getRole().equals("ROLE_ADMIN") ?
                ADMIN.getGrantedAuthorities() : (
                    user.getRole().equals("ROLE_SHOPPER") ?
                    SHOPPER.getGrantedAuthorities() : (
                        user.getRole().equals("ROLE_CUSTOMER") ?
                            CUSTOMER.getGrantedAuthorities() : null
                    )
                );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return active;
        return true;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}