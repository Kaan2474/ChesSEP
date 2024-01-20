package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.ChessEngine.ChessGameType;
import com.ChesSEP.ChesSEP.ChessGame.ChessGame;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChessGame {

    @Id
    @GeneratedValue
    private Long gameID;

    @Column
    private String name;

    @Column
    private Long playerWhiteID;

    @Column
    private Long playerBlackID;

    @Column
    private Long matchLength;

    @Column
    private Long startTime;

    @Column
    private String result;

    @Column
    private boolean whiteLastFrameSeen;

    @Column
    private boolean blackLastFrameSeen;

    @Column
    private long pgnId;
    
    @Column
    @Enumerated(EnumType.STRING)
    private ChessGameType type;

}
