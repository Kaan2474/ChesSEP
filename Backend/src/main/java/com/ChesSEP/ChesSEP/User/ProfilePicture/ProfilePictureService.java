package com.ChesSEP.ChesSEP.User.ProfilePicture;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePictureService {

    @Autowired
    private ProfilePictureRepository profilePictureRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenService tokenService;

    private User getUserFromToken(String token) {
        return userRepository.findByEmail(tokenService.extractEmail(token.substring(7)));
    }

    public String uplpadImage(String token, MultipartFile file) throws IOException {

            Picture dbImage = Picture.builder()
                    .fileName(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(file.getBytes())
                    .build();
            profilePictureRepository.save(dbImage);

            User me = getUserFromToken(token);
            if(me.getPicture() == null) {
                me.setPicture(dbImage);
                userRepository.save(me);
            }else{
                deletePicture(token);
                me.setPicture(null);
                me.setPicture(dbImage);
                userRepository.save(me);
            }

            if(dbImage.getId() != null){
            return ("successfully uploaded" + me.getPicture());
        }
        return null;
    }
    public void deletePicture(String token){
        profilePictureRepository.deleteById(getUserFromToken(token).getPicture().getId());
    }

}


