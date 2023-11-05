package com.ChesSEP.ChesSEP.ChessGame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

    @Query("FROM MatchRequest WHERE matchRequestID.RequestorID IN ?2 AND matchRequestID.InvitedID IN ?1")
    MatchRequest secondRequest(Long requestor, Long invited);

    @Query("FROM MatchRequest WHERE matchRequestID.RequestorID IN ?1")
    MatchRequest searchRequest(Long requestor);
}
