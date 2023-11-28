package com.ChesSEP.ChesSEP.ChessClub;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChessClub {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private Long chatId;
}
