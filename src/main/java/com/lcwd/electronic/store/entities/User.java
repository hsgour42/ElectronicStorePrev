package com.lcwd.electronic.store.entities;

import io.micrometer.observation.annotation.Observed;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private String userId;
    @Column(name = "user_name",length = 50,nullable = false)
    private String name;
    @Column(name = "user_email",length = 80,unique = true)
    private String email;
    @Column(name = "user_password",length = 500)
    private String password;
    @Column(name = "user_gender",length = 10)
    private String gender;
    @Column(name = "user_about",length = 1000)
    private String about;
    @Column(name = "user_image_name",length = 100)
    private String imageName;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
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
