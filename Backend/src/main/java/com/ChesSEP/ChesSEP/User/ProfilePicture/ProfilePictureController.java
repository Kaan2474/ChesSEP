package com.ChesSEP.ChesSEP.User.ProfilePicture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ProfilePictureController {

    @Autowired
    private ProfilePictureService profilePictureService;

    @PostMapping("/profile/picture")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile file) throws IOException {
        String response = profilePictureService.uplpadImage(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/profile/picture/{name}")
    public ResponseEntity<?>  getImageByName(@PathVariable("name") String name){
        byte[] image = profilePictureService.getImage(name);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }
}
