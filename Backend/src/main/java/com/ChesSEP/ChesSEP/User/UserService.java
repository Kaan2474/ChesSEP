package com.ChesSEP.ChesSEP.User;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserWrapper;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserWrapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public String registerUser(@NonNull UserWrapper user){
        User assembledUser=User.builder()
            .vorname(user.getVorname())
            .nachname(user.getNachname())
            .email(user.getEmail())
            .passwort(passwordEncoder.encode(user.getPasswort()))
            .geburtsdatum(user.getGeburtsdatum())
            .elo(500)
            .role(Role.USER)
            .build();

        userRepository.save(assembledUser);

        String authToken=tokenService.GenerateToken(assembledUser);

        return authToken;
    }

    public String authenticate(AuthUserWrapper user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPasswort()));
            List<User> foundUser = userRepository.findByEmail(user.getEmail());
            String authToken=tokenService.GenerateToken(foundUser.get(0));

            return authToken;
        }catch(Exception e){
            return "Fehler beim Authentifizieren: "+e;
        }
    }

    public List<User> findUserbyEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}
