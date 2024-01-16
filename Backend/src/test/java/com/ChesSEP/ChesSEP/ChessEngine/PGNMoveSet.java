package com.ChesSEP.ChesSEP.ChessEngine;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PGNMoveSet {




    @Test
    @Order(1)
    public void normalMove(){

        /*
        KoordinatenSystem ist in unserem Fall (x = Zeile
         */
        //Arrange
        List<ChessOperation> zuege = new ArrayList<>();
        ChessPiece movingBauer1 = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessOperation bauer1 = new ChessOperation(6,0,5,0, movingBauer1, null, "");
        zuege.add(bauer1);

        //act
        String move ="";
        for(ChessOperation pointer: zuege){
            move = pointer.toStringKomprimiert();
        }

        //Assert

        Assertions.assertEquals("a3", move);

    }

}
