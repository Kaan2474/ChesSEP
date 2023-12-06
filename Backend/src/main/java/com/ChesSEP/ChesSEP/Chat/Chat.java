package com.ChesSEP.ChesSEP.Chat;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;

    private long ownerId; //Der Gründer eines ChessClubs = ownerId;

    private long recipientId; //nur bei private Chat

    private String chessClubName; //nur bei ChessClub
    private String privateGroupName; //nur bei privaten Gruppen

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @ElementCollection
    private List<Long> user = new ArrayList<>(); //Hier kommt die ID des Empfängers oder einzelner Em
}
