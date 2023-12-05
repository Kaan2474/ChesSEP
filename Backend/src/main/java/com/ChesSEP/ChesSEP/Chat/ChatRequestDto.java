package com.ChesSEP.ChesSEP.Chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatRequestDto {


    private Long ownerId;
    private Long recipientId;
    private Long chessClubId;
    private String chessClubName;
    private String groupName;
    private List<Long> user;
    private ChatType type;
    private String content;

}
