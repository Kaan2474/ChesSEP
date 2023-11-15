package com.ChesSEP.ChesSEP.Friendlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("FROM Friend WHERE (friendID.FriendID1 = ?1 OR friendID.FriendID1 = ?2) AND (friendID.FriendID2 = ?1 OR friendID.FriendID2 = ?2 OR friendID.FriendID1 =?1 OR friendID.FriendID2 =?2) AND type = 'FRIEND'")
    Friend getFriends(Long friend1, Long friend2);

    @Query("FROM Friend WHERE ((friendID.FriendID1 = ?1 AND friendID.FriendID2 = ?2) OR (friendID.FriendID1 = ?2 AND friendID.FriendID2 = ?1)) AND type = 'REQUEST' ")
    Friend getRequest(Long friend1, Long friend2);

    @Query("FROM Friend WHERE (friendID.FriendID1 = ?1 OR friendID.FriendID2 = ?1) AND type = 'FRIEND' ")
    List<Friend> getFriendlist(Long friend1);

    @Query("FROM Friend WHERE (friendID.FriendID2 = ?1) AND type = 'REQUEST' ")
    List<Friend> getFriendRequests(Long userID);


}
