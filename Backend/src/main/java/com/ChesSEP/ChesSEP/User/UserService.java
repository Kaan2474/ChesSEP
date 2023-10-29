package com.ChesSEP.ChesSEP.User;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public String registerUser(@NonNull UserRequestHolder user){

        if(userRepository.findByEmail(user.getEmail())!=null){
            return user.getEmail()+": Email Existiert Bereits";
        }

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

    public String authenticate(AuthUserRequestHolder user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPasswort()));
            User foundUser = userRepository.findByEmail(user.getEmail());
            String authToken=tokenService.GenerateToken(foundUser);

            return authToken;
        }catch(Exception e){
            return "Fehler beim Authentifizieren: "+e;
        }
    }

    public User findUserbyEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findUserById(Long id){
        return userRepository.findUserById(id);
    }

    /*
    Beim Run der App wird ein Fehler erzeugt, "Logging Failure" - darf also nur auf abfrage laufen, sonst fehler
    @GetMapping("/whoAmI")
    public ResponseEntity<Long> whoAmI(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(userService.findUserbyEmail(tokenService.extractEmail(token.substring(7))).getId());
    }

     */
}
