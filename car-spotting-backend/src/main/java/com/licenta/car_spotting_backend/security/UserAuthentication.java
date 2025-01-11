package com.licenta.car_spotting_backend.security;

import com.licenta.car_spotting_backend.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication implements Authentication {

    private final User user;
    private boolean authenticated = true;

    public UserAuthentication(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Poți extinde această metodă pentru a returna rolurile utilizatorului, dacă folosești un sistem de roluri
        return null;
    }

    @Override
    public Object getCredentials() {
        return null; // Credențialele nu sunt necesare aici
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user; // Returnează utilizatorul
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return user.getUsername(); // Returnează numele utilizatorului
    }

}
