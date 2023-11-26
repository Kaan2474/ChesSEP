package com.ChesSEP.ChesSEP.User.ProfilePicture;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProfilePictureController {

    private final ProfilePictureService profilePictureService;

    @PostMapping("profile/picture/v2")
    public void uploadPicture(@RequestHeader(value = "Authorization")String jwtToken,@RequestParam("user-profile-view") MultipartFile file) throws IOException {
        profilePictureService.uplpadImage(jwtToken, file);
    }

}
