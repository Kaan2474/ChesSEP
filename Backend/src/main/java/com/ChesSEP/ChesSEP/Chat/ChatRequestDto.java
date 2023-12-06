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


    private long ownerId;
    private long recipientId;
    private long chessClubId;
    private String chessClubName;
    private String groupName;
    private List<Long> user;
    private ChatType type;
    private String content;
    private long chatId;

}
