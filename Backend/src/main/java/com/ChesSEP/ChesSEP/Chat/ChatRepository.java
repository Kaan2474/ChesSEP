package com.ChesSEP.ChesSEP.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    //Suche ein Chat, wo ownerId = ?1 und wo eine aus User_list = ?2 (soll einen PrivatenChat zeigen)
    //@Query("SELECT x FROM Chat x JOIN x.ownerId u WHERE x.ownerId = ?1 AND u = ?2 AND x.type ='PRIVATE'")
    //Chat getPrivateChatBetween(Long owner, Long friend, ChatType type);

    @Query("FROM Chat WHERE privateGroupName = ?1")
    Chat findChatByGroupName(String groupName);


    @Query("FROM Chat WHERE chatId = ?1")
    Chat findChatByChatId(Long chatId);

    //Unterhaltung zwischen 2 Personen
    @Query("FROM Chat WHERE (ownerId = ?1 AND recipientId = ?2) OR (ownerId = ?2 AND recipientId = ?1)")
    Chat getPrivateChat(Long owner, Long friend);


    @Query("FROM Chat WHERE chessClubName = ?1")
    Chat findChatByClubName(String clubName);

    @Query("FROM Chat WHERE chatId = ?1")
    List<Long> memberOfGroupChat(Long chatId);

    @Query("SELECT c FROM Chat c JOIN c.user u WHERE c.chatId = ?1 AND u = ?2")
    Chat findByChatIdAndUserId(long chatId, long userId);

   @Query("FROM Chat WHERE ownerId = ?1 ")
    List<Chat> findAllChatsOfUserId(long userId);

  @Query("FROM Chat WHERE ownerId = ?1 AND type = ?2")
   List<Chat> findAllGroupChatsOfUserId(long userId, ChatType chatType);


}
