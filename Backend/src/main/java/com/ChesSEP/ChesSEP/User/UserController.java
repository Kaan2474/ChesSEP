package com.ChesSEP.ChesSEP.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
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
    public ResponseEntity<String> resgisterUser(@RequestBody UserRequestHolder  user){
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> loginUser(@RequestBody AuthUserRequestHolder user){
        return ResponseEntity.ok(userService.authenticate(user));
    }

    @PostMapping("/twoFactor")
    public ResponseEntity<String> twoFactor(@RequestBody AuthUserRequestHolder user){
        return ResponseEntity.ok(userService.checkTwoFactor(user.getOtp()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserRequestHolder> getUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.convetToRequestHolder(userService.findUserById(userId)));
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
