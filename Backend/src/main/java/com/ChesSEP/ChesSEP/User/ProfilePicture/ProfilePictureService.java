package com.ChesSEP.ChesSEP.User.ProfilePicture;

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

    public String uplpadImage(MultipartFile file) throws IOException {

            Picture dbImage = Picture.builder()
                    .fileName(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(file.getBytes())
                    .build();
            profilePictureRepository.save(dbImage);

            if(dbImage.getId() != null){
            return ("successfully uploaded");
        }
        return null;
    }

    public byte[] getPicture(String filename){
        return profilePictureRepository.findbyName(filename).getImageData();
    }
}


