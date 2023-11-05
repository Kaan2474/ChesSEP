package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Friendlist.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessgameRepository extends JpaRepository<ChessGame, Long> {

    @Query("FROM ChessGame WHERE playerBlackID = ?1 or playerWhiteID = ?1")
    ChessGame findGame(String user);
}
