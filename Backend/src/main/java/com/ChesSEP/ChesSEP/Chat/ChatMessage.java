package com.ChesSEP.ChesSEP.Chat;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long messageId;

    @Column
    private long senderId;

    @Column
    private long chatId;

    @Column
    private long time;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private ChatMessageStatus chatMessageStatus;
}
