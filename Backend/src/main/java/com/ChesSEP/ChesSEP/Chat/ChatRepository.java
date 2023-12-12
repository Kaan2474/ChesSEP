package com.ChesSEP.ChesSEP.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("FROM Chat WHERE privateGroupName = ?1")
    Chat findChatByGroupName(String groupName);

    @Query("FROM Chat WHERE chatId = ?1")
    Chat findChatByChatId(Long chatId);

    //Unterhaltung zwischen 2 Personen
    @Query("FROM Chat WHERE (ownerId = ?1 AND recipientId = ?2) OR (ownerId = ?2 AND recipientId = ?1)")
    Chat getPrivateChat(Long owner, Long friend);

    @Query("FROM Chat WHERE chessClubName = ?1")
    Chat findChatByClubName(String clubName);

    @Query("SELECT c FROM Chat c JOIN c.user u WHERE c.chatId = ?1 AND u = ?2")
    Chat findByChatIdAndUserId(long chatId, long userId);

    @Query("SELECT c FROM Chat c JOIN c.user u WHERE u = ?1 AND c.type = 'GROUP'")
    List<Chat> findAllGroupChatsOfUserId(long userId);



}
