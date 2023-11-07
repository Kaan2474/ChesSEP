package com.ChesSEP.ChesSEP.User.ProfilePicture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilePictureRepository extends JpaRepository<Picture, Long> { //To Fetch images from db by Filename
    Optional<Picture> findByFileName(String filename);
}
