package com.ChesSEP.ChesSEP.Chat;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
class MessageId implements Serializable {Long senderId; Long chatId; Long time;}

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessage {

    @EmbeddedId
    MessageId messageId;

    private String content;

    private Boolean read;
}
