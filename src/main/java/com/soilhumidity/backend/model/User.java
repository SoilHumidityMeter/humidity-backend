package com.soilhumidity.backend.model;

import com.soilhumidity.backend.enums.ERole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.URL;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@DynamicUpdate
public class User implements UserDetails, Serializable {
    @Id
    @Getter
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(nullable = false)
    private String surname;

    @Getter
    @Setter
    @NonNull
    @Column(unique = true, nullable = false)
    private String email;

    @Getter
    @Setter
    @Column(length = 15, unique = true)
    private String phoneNumber;

    @Setter
    @NonNull
    @Column(nullable = false, length = 60)
    private String password;

    @Getter
    @Column(nullable = false)
    @CreationTimestamp
    private Date createdOn;

    @Getter
    @Setter
    @Column(nullable = false)
    private int role;

    @Column(nullable = false)
    private boolean confirmed = false;

    @Column(nullable = false)
    private boolean banned = false;

    @Column(nullable = false)
    private boolean disabled = false;

    @Getter
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private final Set<MobileDevice> mobileDevices = new HashSet<>();

    @Getter
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private final Set<UserDevice> userDevices = new HashSet<>();

    @Getter
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private final Set<VerificationCode> verificationCodes = new HashSet<>();

    @URL
    @Getter
    @Setter
    @Column
    private String profilePicture;

    @Formula("concat(name,' ',surname)")
    @Getter
    private String fullName;

    public User(
            String name,
            String surname,
            @NotNull String email,
            String phoneNumber,
            @NotNull String password,
            int role,
            String profilePicture
    ) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public User(
            Long id,
            String name,
            String surname,
            @NotNull String email,
            @NotNull String phoneNumber,
            String password,
            int role,
            String profilePicture,
            Date createdOn,
            boolean banned,
            boolean confirmed
    ) {
        this(name, surname, email, phoneNumber, password, role, profilePicture);
        this.id = id;
        this.createdOn = createdOn;
        this.banned = banned;
        this.confirmed = confirmed;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new ERole(ERole.stringValueOf(role)));
    }

    @NotNull
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * AuthenticationManager uses this function to check user's account activated or not.
     *
     * @return Is account not banned
     */
    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    /**
     * We do not have any subscription feature, so we are returning
     * by looking that user activated his account or not.
     * May be can be look user's created on to block him
     *
     * @return Is account confirmed?
     */
    @Override
    public boolean isAccountNonExpired() {
        return confirmed;
    }

    /**
     * We do not use expiration issue so works with confirmed variable.
     *
     * @return Is account confirmed?
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return confirmed;
    }

    /**
     * This function returns not banned because AuthenticationManager user
     * isEnabled to check user is disabled or not.
     *
     * @return Is account not disabled
     */
    @Override
    public boolean isEnabled() {
        return !disabled;
    }

    /**
     * Activate a user for first login
     */
    public void confirm() {
        confirmed = true;
    }

    /**
     * Deactivate a user account
     */
    public void deactivate() {
        confirmed = false;
    }

    /**
     * Disable a user account
     */
    public void disable() {
        disabled = true;
    }

    /**
     * Enable a user account
     */
    public void enable() {
        disabled = false;
    }
}
