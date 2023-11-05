package com.ChesSEP.ChesSEP.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.Security.RequestHolder.RequestHolder;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> resgisterUser(@RequestBody UserRequestHolder  user){
        Boolean result=userService.registerUser(user);

        if(result){
            return new ResponseEntity<Boolean>(true,HttpStatus.OK);
        }

        return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> loginUser(@RequestBody AuthUserRequestHolder user){
        Boolean result=userService.authenticate(user);

        if(result){
            return new ResponseEntity<Boolean>(true,HttpStatus.OK);
        }

        return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/twoFactor")
    public ResponseEntity<RequestHolder<String>> twoFactor(@RequestBody AuthUserRequestHolder user){

        RequestHolder<String> result=new RequestHolder<String>(true,userService.checkTwoFactor(user.getTwoFactor()));
        if(!result.getObject().equals("0")){
            return new ResponseEntity<RequestHolder<String>>(result,HttpStatus.OK);
        }

        return new ResponseEntity<RequestHolder<String>>(result,HttpStatus.BAD_REQUEST);
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
