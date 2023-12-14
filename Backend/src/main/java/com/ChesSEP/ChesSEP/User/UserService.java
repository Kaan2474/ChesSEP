package com.ChesSEP.ChesSEP.User;

import java.io.IOException;
import java.util.List;

import com.ChesSEP.ChesSEP.TwoFactorAuthentication.OtpService;
import com.ChesSEP.ChesSEP.User.ProfilePicture.Picture;
import com.ChesSEP.ChesSEP.User.ProfilePicture.ProfilePictureRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;

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
    private EmailValidator emailValidator=new EmailValidator();


    public User getSender(){
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String registerUser(UserRequestHolder user, MultipartFile bild) throws IOException{

        if((userRepository.findByEmail(user.getEmail())!=null)||!emailValidator.isEmailValid(user.getEmail())){
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
            .compleatedPuzzles(0L)
            .build();

        userRepository.save(assembledUser);

        if(bild!=null){
            pictureRepository.save(Picture.builder()
                .id(userRepository.findByEmail(user.getEmail()).getId())
                .fileName(bild.getOriginalFilename())
                .type(bild.getContentType())
                .imageData(bild.getBytes())
                .build());
        }

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

    public UserRequestHolder getUserByToken(){
        User sender =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return convetToRequestHolder(sender);
    }

    public UserRequestHolder convetToRequestHolder(User user){
        if(user ==null){
            return null;
        }

        UserRequestHolder holder;
        byte[] picture=null;

        if(pictureRepository.getPictureByID(user.getId())==null)
            picture=null;
        else{
            picture=pictureRepository.getPictureByID(user.getId()).getImageData();
        }
        
        holder=UserRequestHolder.builder()
        .id(user.getId())
        .vorname(user.getVorname())
        .nachname(user.getNachname())
        .email(user.getEmail())
        .geburtsdatum(user.getGeburtsdatum())
        .elo(user.getElo())
        .profilbild(picture)
        .privacy(user.getFriendlistPrivacy().name())
        .clubId(user.getClubId())
        .compleatedPuzzles(user.getCompleatedPuzzles())
        .build();
       
        return holder;

    }

    public void  changeFriendListPrivacy(){
        User thisUser=getSender();
        if(thisUser.getFriendlistPrivacy()==Privacy.OEFFENTLICH){
            thisUser.setFriendlistPrivacy(Privacy.PRIVAT);
        }else{
            thisUser.setFriendlistPrivacy(Privacy.OEFFENTLICH);
        }

        userRepository.save(thisUser);
    }

    public int getMyLeaderboardPosition(){
        return userRepository.getMyLeaderboardPosition(getSender().getElo())+1;
    }

    public UserRequestHolder[] getLeaderboard(){
        List<User> list = userRepository.getLeaderboard();
        UserRequestHolder[] arr = new UserRequestHolder[list.size()];

        for (int i = 0; i < list.size(); i++) {
            arr[i] = convetToRequestHolder(list.get(i));
        }
        return arr;
    }
}
