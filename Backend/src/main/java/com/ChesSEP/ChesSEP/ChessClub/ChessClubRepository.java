package com.ChesSEP.ChesSEP.ChessClub;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessClubRepository extends JpaRepository<ChessClub, Long> {

    @Query("FROM ChessClub WHERE name = ?1")
    ChessClub findChessClubByName(String clubName);

    @Query("FROM ChessClub WHERE name = ?1")
    ChessClub findChessClubById(Long clubId);

    @Query("FROM ChessClub")
    List<ChessClub> getAllChessClubs();

}
