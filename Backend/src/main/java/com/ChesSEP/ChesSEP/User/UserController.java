package com.ChesSEP.ChesSEP.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> resgisterUser(@RequestBody UserRequestHolder  user)throws JsonProcessingException{
        String result=userService.registerUser(user);

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
