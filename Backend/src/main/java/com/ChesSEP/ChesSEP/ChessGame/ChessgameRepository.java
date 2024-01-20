package com.ChesSEP.ChesSEP.ChessGame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessgameRepository extends JpaRepository<ChessGame, Long> {

    @Query("FROM ChessGame WHERE (playerWhiteID = ?1 and playerBlackID = ?2) and startTime = ?3")
    ChessGame findGame(Long playerWhiteID, Long playerBlackID, Long startTime);

    @Query("FROM ChessGame WHERE (playerWhiteID = ?1 OR playerBlackID = ?1) AND type = PVP ORDER BY startTime desc limit 3")
    List<ChessGame> letztenDreiGames (Long userId);

}
