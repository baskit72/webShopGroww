package com.twd.webShopGrow.utility;

import com.twd.webShopGrow.entity.OurUsers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;


public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;
    private final OurUsers user;

    public UserDetailsImpl(OurUsers user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming user.getRole() returns a string representation of the role
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
    }
    public static UserDetails build(OurUsers user) {
        return new UserDetailsImpl(user);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Assuming email is the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement logic for account expiration here if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assuming user accounts are not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming user credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // Assuming user accounts are always enabled
    }

    public OurUsers getUser() {
        return user;
    }
}
