package com.ChesSEP.ChesSEP.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    @Query("FROM ChatMessage WHERE chatId = ?1")
    List<ChatMessage> findChatMessagesOf(Chat chatId);

}
