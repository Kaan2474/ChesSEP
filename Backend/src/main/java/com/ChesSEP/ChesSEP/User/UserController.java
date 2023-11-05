package com.ChesSEP.ChesSEP.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
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
    private final TokenService tokenService;
    private ObjectMapper objectMapper=new ObjectMapper();

    @PostMapping("/register")
    public ResponseEntity<String> resgisterUser(@RequestBody UserRequestHolder  user)throws JsonProcessingException{
        String result=userService.registerUser(user);

        if(result.equals("Der User wurde erstellt!")){
            return new ResponseEntity<String>(objectMapper.writeValueAsString(result),HttpStatus.OK);
        }

        return new ResponseEntity<String>(objectMapper.writeValueAsString(result),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> loginUser(@RequestBody AuthUserRequestHolder user) throws JsonProcessingException{
        String result=userService.authenticate(user);

        if(result.equals("Der Code wurde erstellt und die Email versandt!")){
            return new ResponseEntity<String>(objectMapper.writeValueAsString(result),HttpStatus.OK);
        }

        return new ResponseEntity<String>(objectMapper.writeValueAsString(result),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/twoFactor")
    public ResponseEntity<String> twoFactor(@RequestBody AuthUserRequestHolder user)throws JsonProcessingException{

        String result=userService.checkTwoFactor(user);
        if(!result.equals("Das Token konnte nicht Generiert Werden")){
            return new ResponseEntity<String>(objectMapper.writeValueAsString(result),HttpStatus.OK);
        }

        return new ResponseEntity<String>(objectMapper.writeValueAsString(result),HttpStatus.BAD_REQUEST);
    }  

    @GetMapping("/{userId}")
    public ResponseEntity<UserRequestHolder> getUser(@PathVariable Long userId){
        UserRequestHolder result=userService.convetToRequestHolder(userService.findUserById(userId));
        
        if(result!=null){
            return new ResponseEntity<UserRequestHolder>(result,HttpStatus.OK);
        }

        return new ResponseEntity<UserRequestHolder>(result,HttpStatus.BAD_REQUEST);
        
    }



    //For Testing

    @GetMapping("/hello")
    public ResponseEntity<String> HelloWorld(){
        return ResponseEntity.ok("Hello World!");
    }

        //returns id of sender
    @GetMapping("/whoAmI")
    public ResponseEntity<Long> whoAmI(@RequestHeader(value = "Authorization") String token){
        return ResponseEntity.ok(userService.findUserbyEmail(tokenService.extractEmail(token.substring(7))).getId());
    }
}
