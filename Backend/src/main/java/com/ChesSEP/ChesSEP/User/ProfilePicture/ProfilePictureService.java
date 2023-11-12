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

    public void uplpadImage(String token, MultipartFile file) throws IOException {
            User user = getUserFromToken(token);

            if(profilePictureRepository.getPictureByID(user.getId())==null){
                Picture dbImage = Picture.builder()
                    .id(user.getId())
                    .fileName(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(file.getBytes())
                    .build();
                
                profilePictureRepository.save(dbImage);

                return;
            }

            Picture currentPicture = profilePictureRepository.getPictureByID(user.getId());

            currentPicture.setImageData(file.getBytes());
            currentPicture.setFileName(file.getName());
            currentPicture.setType(file.getContentType());

            profilePictureRepository.save(currentPicture);
    }
}


