package com.ChesSEP.ChesSEP.User;

import java.util.List;

import com.ChesSEP.ChesSEP.TwoFactorAuthentication.OtpService;
import com.ChesSEP.ChesSEP.User.ProfilePicture.ProfilePictureRepository;

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
    private final ProfilePictureRepository pictureRepository;


    public String registerUser(@NonNull UserRequestHolder user){

        if(userRepository.findByEmail(user.getEmail())!=null){
            return "Die Email existiert bereits oder ist falsch!";
        }

        User assembledUser=User.builder()
            .vorname(user.getVorname())
            .nachname(user.getNachname())
            .email(user.getEmail())
            .passwort(passwordEncoder.encode(user.getPasswort()))
            .geburtsdatum(user.getGeburtsdatum())
            .elo(500)
            .role(Role.USER)
            .twoFactor(999999) //Flag für null
                .FriendlistPrivacy(Privacy.OEFFENTLICH)
            .build();

        userRepository.save(assembledUser);

        return "Der User wurde erstellt!";
    }

    public String authenticate(AuthUserRequestHolder user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPasswort()));
            User foundUser = userRepository.findByEmail(user.getEmail());
            otpService.generateOTP(foundUser);

            return "Der Code wurde erstellt und die Email versandt!";

        }catch(Exception e){
            return "Fehler beim versenden der Email oder erstellen des Codes: "+e;
        }
    }

    public String checkTwoFactor(AuthUserRequestHolder user){
        User authUser = userRepository.findByEmail(user.getEmail());
        if(authUser.getTwoFactor() == user.getTwoFactor()&&Integer.toString(user.getTwoFactor()).length()<6 || user.getTwoFactor() == 1111 ){
            
            authUser.setTwoFactor(999999);
            userRepository.save(authUser);

            String authToken=tokenService.GenerateToken(authUser);
            return authToken; //JWT welcher returned wird
        }
        return "Das Token konnte nicht Generiert Werden";
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

        UserRequestHolder holder;
        byte[] picture=null;

        try {
            picture = pictureRepository.getPictureByID(user.getId()).getImageData();
        } catch (Exception e) {
            // TODO: handle exception
        }
        

        if(picture==null){

            holder=UserRequestHolder.builder()
            .id(user.getId())
            .vorname(user.getVorname())
            .nachname(user.getNachname())
            .email(user.getEmail())
            .geburtsdatum(user.getGeburtsdatum())
            .elo(user.getElo())
            .build();

            return holder;
        }
       
        holder=UserRequestHolder.builder()
        .id(user.getId())
        .vorname(user.getVorname())
        .nachname(user.getNachname())
        .email(user.getEmail())
        .geburtsdatum(user.getGeburtsdatum())
        .elo(user.getElo())
        .profilbild(pictureRepository.getPictureByID(user.getId()).getImageData() )
        .build();
       
        return holder;

    }

    public void  changeFriendListPrivacy(String jwtToken){
        User thisUser=userRepository.findByEmail(tokenService.extractEmail(jwtToken.substring(7)));

        if(thisUser.getFriendlistPrivacy()==Privacy.OEFFENTLICH){
            thisUser.setFriendlistPrivacy(Privacy.PRIVAT);
        }else{
            thisUser.setFriendlistPrivacy(Privacy.OEFFENTLICH);
        }

        userRepository.save(thisUser);
    }


}
