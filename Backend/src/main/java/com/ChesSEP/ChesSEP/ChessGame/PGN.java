package com.ChesSEP.ChesSEP.ChessGame;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PGN {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pgnId;

    @Column
    private String pgnInfo;

}
