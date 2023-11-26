package com.ChesSEP.ChesSEP.User.ProfilePicture;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureRepository extends JpaRepository<Picture, Long> {
    @Query("FROM Picture p WHERE p.id = ?1 ")
    public Picture getPictureByID(Long id);
}
