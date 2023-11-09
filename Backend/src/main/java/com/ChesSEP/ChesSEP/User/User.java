package com.ChesSEP.ChesSEP.User;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import com.ChesSEP.ChesSEP.User.ProfilePicture.Picture;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column
    private String vorname;
    
    @Column
    private String nachname;

    @Column
    private int twoFactor;

    @Column
    private String email;

    @Column
    private String passwort;

    @Column
    private Date geburtsdatum;

    @Column
    private int elo;

    @OneToOne(cascade = CascadeType.ALL)
    private Picture profilbild;

    @Column
    private final int supercode = 1111; //Der für die Abnahme erforderliche Super Sicherheitscode

    @Column
    @Enumerated(EnumType.STRING)
    private Privacy FriendlistPrivacy;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;


    //Friendlist friendlist

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getPassword() {
        return passwort;
    }
    @Override
    public String getUsername() {
        return email;
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

    public Role getRole(){
        return role;
    }


}
