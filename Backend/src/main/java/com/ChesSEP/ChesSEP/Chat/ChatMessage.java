package com.ChesSEP.ChesSEP.Chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
class MessageId implements Serializable {Long messageId; Long senderId; Long time;}

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessage {

    @EmbeddedId
    MessageId messageId;

    private Long chatId;

    private String content;

    private Boolean read;
}
