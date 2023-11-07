package com.ChesSEP.ChesSEP.User.ProfilePicture;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfilePictureService {

    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    public String uplpadImage(MultipartFile file) throws IOException {

        profilePictureRepository.save(Picture.builder()
                .fileName(file.getOriginalFilename())
                .imageData(PictureConfig.compressImage(file.getBytes()))
                .build());
        return ("Image uploaded successfully: " + file.getOriginalFilename());
    }

    @Transactional
    public Picture getInfoByImageByName(String name) {
        Optional<Picture> dbImage = profilePictureRepository.findByFileName(name);

        return Picture.builder()
                .fileName(dbImage.get().getFileName())
                .imageData(PictureConfig.decompressImage(dbImage.get().getImageData())).build();
    }
    @Transactional
    public byte[] getImage(String name) {
        Optional<Picture> dbImage = profilePictureRepository.findByFileName(name);
        byte[] image = PictureConfig.decompressImage(dbImage.get().getImageData());
        return image;
    }

}
