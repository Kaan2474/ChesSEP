package com.ChesSEP.ChesSEP.User;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import com.ChesSEP.ChesSEP.ChessGame.ChessGame;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private ObjectMapper objectMapper=new ObjectMapper();

    @PostMapping("/register")
    public ResponseEntity<String> resgisterUser(@RequestParam("vorname") String vorname,
                                                @RequestParam("nachname")String nachname,
                                                @RequestParam("email")String email,
                                                @RequestParam("passwort")String passwort,
                                                @RequestParam("geburtsdatum")Date geburtsdatum,
                                                @RequestParam(value = "bild", required=false)MultipartFile bild
    )throws IOException{

        UserRequestHolder user = UserRequestHolder.builder()
            .vorname(vorname)
            .nachname(nachname)
            .email(email)
            .passwort(passwort)
            .geburtsdatum(geburtsdatum)
            .build();

        String result=userService.registerUser(user,bild);

        if(result.equals("Der User wurde erstellt!")){
            return new ResponseEntity<>(objectMapper.writeValueAsString(result),HttpStatus.CREATED);
        }

        return new ResponseEntity<>(objectMapper.writeValueAsString(result),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> loginUser(@RequestBody AuthUserRequestHolder user) throws JsonProcessingException{
        String result=userService.authenticate(user);

        if(result.equals("Der Code wurde erstellt und die Email versandt!")){
            return new ResponseEntity<>(objectMapper.writeValueAsString(result),HttpStatus.OK);
        }

        return new ResponseEntity<>(objectMapper.writeValueAsString(result),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/twoFactor")
    public ResponseEntity<String> twoFactor(@RequestBody AuthUserRequestHolder user)throws JsonProcessingException{

        String result=userService.checkTwoFactor(user);
        if(!result.equals("Das Token konnte nicht Generiert Werden")){
            return new ResponseEntity<>(objectMapper.writeValueAsString(result),HttpStatus.OK);
        }

        return new ResponseEntity<>(objectMapper.writeValueAsString(result),HttpStatus.BAD_REQUEST);
    }  

    @GetMapping("/{userId}")
    public ResponseEntity<UserRequestHolder> getUser(@PathVariable Long userId){
        UserRequestHolder result=userService.convetToRequestHolder(userService.findUserById(userId));
        
        if(result!=null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        }

        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        
    }

    @GetMapping("/byToken")
    public ResponseEntity<UserRequestHolder> getUser(){
        return new ResponseEntity<>(userService.getUserByToken(), HttpStatus.OK);
        
    }

    @GetMapping("/getMyLeaderboardPosition")
    public int getMyLeaderboardPosition(){
        return userService.getMyLeaderboardPosition();
    }

    @GetMapping("/getLeaderboard")
    public ResponseEntity<UserRequestHolder[]> getLeaderboard(){
        return ResponseEntity.ok(userService.getLeaderboard());
    }

    @GetMapping("/privacy")
    public void toggleFriendListPrivacy(){
        userService.changeFriendListPrivacy();
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChessGame>> history(){
        return ResponseEntity.ok(userService.letztenDreiGames());
    }


    //For Testing

    @GetMapping("/hello")
    public ResponseEntity<String> HelloWorld(){
        return ResponseEntity.ok("Hello World!");
    }

        //returns id of sender
    @GetMapping("/whoAmI")
    public ResponseEntity<Long> whoAmI(@RequestHeader(value = "Authorization") String token){
        User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user.getId());
    }
}
