package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.ChessGame.ChessGame;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


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
}
