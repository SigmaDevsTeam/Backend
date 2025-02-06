package com.sigmadevs.testtask.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sigmadevs.testtask.security.entity.Role;
import com.sigmadevs.testtask.security.entity.SignUpMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @ToString.Exclude
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private String image;
    @NotNull
    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;
    @NotNull
    @Builder.Default
    @Column(name = "sign_up_method")
    @Enumerated(value = EnumType.STRING)
    private SignUpMethod signUpMethod = SignUpMethod.form;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.role);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return true;
    }
}