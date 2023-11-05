package com.ChesSEP.ChesSEP.ChessGame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, MatchRequestID> {

    @Query("FROM MatchRequest WHERE matchRequestID.RequestorID IN ?2 AND matchRequestID.InvitedID IN ?1")
    MatchRequest secondRequest(Long requestor, Long invited);

    @Query("FROM MatchRequest WHERE matchRequestID.RequestorID = ?1")
    MatchRequest searchRequest(Long requestor);

    @Query("FROM MatchRequest WHERE matchRequestID.InvitedID = ?1")
    List<MatchRequest> searchInvited(Long invited);
}
