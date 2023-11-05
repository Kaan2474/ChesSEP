package com.ChesSEP.ChesSEP.User;

import java.util.List;

import com.ChesSEP.ChesSEP.TwoFactorAuthentication.OtpService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;


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
                .FriendlistPrivacy(Privacy.OEFFENTLICH)
            .build();

        userRepository.save(assembledUser);

        return "Der Nutzer wurde Registriert!";
    }

    public String authenticate(AuthUserRequestHolder user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPasswort()));
            User foundUser = userRepository.findByEmail(user.getEmail());
            return otpService.generateOTP(foundUser);


        }catch(Exception e){
            return "Fehler beim Authentifizieren: "+e;
        }
    }

    public String checkTwoFactor(String twoFactorToken){
        String [] twoFactor = twoFactorToken.split("_");
        User authUser = userRepository.findUserById(Long.parseLong(twoFactor[1]));
        if(authUser.getTwoFactor() == Long.parseLong(twoFactor[0])&&twoFactor[0].length()<6){
            
            authUser.setTwoFactor(999999);
            userRepository.save(authUser);

            String authToken=tokenService.GenerateToken(authUser);
            return authToken; //JWT welcher returned wird
        }
        return "Fehler bei der Authentifizierung";
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

    public UserRequestHolder convetToRequestHolder(User user){
        if(user ==null){
            return null;
        }
       
        UserRequestHolder holder=UserRequestHolder.builder()
        .id(user.getId())
        .vorname(user.getVorname())
        .nachname(user.getNachname())
        .email(user.getEmail())
        .geburtsdatum(user.getGeburtsdatum())
        .profilbild(user.getProfilbild())
        .elo(user.getElo())
        .build();
       
        return holder;

    }


}
