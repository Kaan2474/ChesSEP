package com.ChesSEP.ChesSEP.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    //Gibt auch alle Nachrichten aus..
    @Query("FROM ChatMessage WHERE chatId = ?1")
    List<ChatMessage> findChatMessagesOf(long chatId);

   /* @Query("SELECT max(messageId.time) FROM ChatMessage WHERE messageId.chatId = ?1")
    ChatMessage findLatestMessage(long chatId);
    */

    //Gibt alle Nachrichten aus, die vor max.Time gepostet wurden
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.time = (SELECT max(cm2.time) FROM ChatMessage cm2)")
    ChatMessage findLatestMessage(long chatId);


    //Gibt nur die letzte(n) Nachricht aus, die nach "time" geschrieben wurden
    @Query("FROM ChatMessage WHERE chatId = ?1 AND ?2 < time")
    List<ChatMessage> findNewMessageOf(long chatId, long lastMessageTime);

    // Soll nur die Nachricht(en) ausgeben, die einen Boolean wert von .. haben
    @Query("FROM ChatMessage WHERE chatId = ?1 AND senderId = ?2 ")
    List<ChatMessage> myWrittenMessage(long chatId, long senderId);
}
