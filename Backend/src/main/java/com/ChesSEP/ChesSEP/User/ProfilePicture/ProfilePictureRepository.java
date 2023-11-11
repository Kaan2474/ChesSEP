package com.ChesSEP.ChesSEP.User.ProfilePicture;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProfilePictureRepository extends JpaRepository<Picture, Long> { //To Fetch images from db by Filename
    Picture findbyName(String fileName);
}
