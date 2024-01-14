package com.ChesSEP.ChesSEP.ChessGame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PgnRepository extends JpaRepository<PGN, Long> {

    @Query("FROM PGN WHERE pgnId = ?1")
    PGN findPGNByPgnId(long pgnId);
}
