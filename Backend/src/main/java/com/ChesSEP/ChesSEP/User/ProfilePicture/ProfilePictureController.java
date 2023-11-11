package com.ChesSEP.ChesSEP.User.ProfilePicture;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ProfilePictureController {

    @Autowired
    private ProfilePictureService profilePictureService;
    @Autowired
    private UserService userService;
    @Autowired
    TokenService tokenService;

    @PostMapping("/profile/picture")
    public ResponseEntity<String> uploadImage(@RequestHeader(value = "Authorization")@RequestParam("user-profile-view") MultipartFile file) throws IOException {
        String response = profilePictureService.uplpadImage(file);

        if (response.equals("successfully uploaded")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
