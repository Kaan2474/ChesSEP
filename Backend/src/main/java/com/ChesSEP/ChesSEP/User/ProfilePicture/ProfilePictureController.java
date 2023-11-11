package com.ChesSEP.ChesSEP.User.ProfilePicture;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ProfilePictureController {

    @Autowired
    private ProfilePictureService profilePictureService;

    @Autowired
    TokenService tokenService;


    @PostMapping("profile/picture/v2")
    public void uploadPicture(@RequestHeader(value = "Authorization")String jwtToken,@RequestParam("user-profile-view") MultipartFile file) throws IOException {
        profilePictureService.uplpadImage(jwtToken, file);
    }

}
