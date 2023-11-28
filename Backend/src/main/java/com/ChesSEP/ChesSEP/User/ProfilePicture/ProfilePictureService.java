package com.ChesSEP.ChesSEP.User.ProfilePicture;

import com.ChesSEP.ChesSEP.User.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
;

@Service
@RequiredArgsConstructor
public class ProfilePictureService {

    private final ProfilePictureRepository profilePictureRepository;

    private User getSender(){
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void uplpadImage(MultipartFile file) throws IOException {
            User user = getSender();

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


