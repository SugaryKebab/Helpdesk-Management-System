package sugarykebab.helpdesk.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AppUserDetail implements UserDetails {

    private final AppUser appUser;


    public AppUserDetail(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public Role getRole(){
        return appUser.getRole();
    }

    // --- Implementation of UserDetails Methods ---

    /**
     * Returns the authorities granted to the user (i.e., roles).
     * NOTE: This example returns an empty list, assuming roles are handled elsewhere or not yet defined.
     * In a real application, you'd fetch roles associated with the AppUser.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You would typically map a list of roles (e.g., "ADMIN", "USER") to authorities here.
        // For now, we return an empty list or a default role.

        // Example with a hardcoded default role:
        // return List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name()));
    }

    /**
     * Returns the password used to authenticate the user.
     */
    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     * We use the AppUser's email as the username.
     */
    @Override
    public String getUsername() {
        return appUser.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        // Assuming accounts don't expire for simplicity
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     */
    @Override
    public boolean isAccountNonLocked() {
        // Assuming accounts are not locked for simplicity
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // Assuming credentials don't expire for simplicity
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * Uses the AppUser's isActive field.
     */
    @Override
    public boolean isEnabled() {
        // Handle potential null to ensure safety, then check the active status.
        return appUser.getIsActive() != null && appUser.getIsActive();
    }
}
