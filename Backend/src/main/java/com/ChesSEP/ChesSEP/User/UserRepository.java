package com.ChesSEP.ChesSEP.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    @Query("FROM User WHERE email = ?1")
    User findByEmail(String email);

    @Query("FROM User WHERE id = ?1")
    User findUserById(Long id);

    @Query("SELECT COUNT(*) FROM User WHERE elo > ?1")
    int getMyLeaderboardPosition(int elo);

    @Query("FROM User ORDER BY elo LIMIT 5")
    List<User> getLeaderboard();

    @Query("FROM User WHERE clubId = ?1")
    List<User> getChessClubMember(Long id);
    
    
} 