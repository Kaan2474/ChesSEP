package com.ChesSEP.ChesSEP.ChessEngine;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoveValidatorTest {
    
    BoardManager boardManager=new BoardManager();
    ChessBoard chessboard;

    @Test
    @Order(1)
    public void defaultPosCount(){
        //Arrange
        boardManager.startNewTest(boardManager.getDefaultFEN());
        this.chessboard=boardManager.getManagedBoard();

        //Act
        int counter1=countMoves(chessboard.chessBoard, 1,Color.WHITE);
        int counter2=countMoves(chessboard.chessBoard, 2,Color.WHITE);
        //int counter3=countMoves(chessboard.chessBoard, 3,Color.WHITE);
        //int counter4=countMoves(chessboard.chessBoard, 4,Color.WHITE);
        //int counter5=countMoves(chessboard.chessBoard, 5,Color.WHITE);
        //int counter6=countMoves(chessboard.chessBoard, 6,Color.WHITE);

        //Assert

        Assertions.assertEquals(20,counter1);
        Assertions.assertEquals(400,counter2);
        //Assertions.assertEquals(8902,counter3);
        //Assertions.assertEquals(197.281,counter4);
        //Assertions.assertEquals(4865609,counter5);
        //Assertions.assertEquals(119060324,counter6);
    }

    private int countMoves(ChessPiece[][] board,int depth,Color currentPlayerColor){
        if(depth==0)
        return 1;

        int counter=0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j]==null)
                continue;

                int[][] validCoords=chessboard.exportMoves(i,j,board,currentPlayerColor);

                if(validCoords==null)
                    continue;

                for (int k = 0; k < validCoords.length; k++) {
                    for (int k2 = 0; k2 < validCoords[k].length; k2++) {
                        if(validCoords[k][k2]==0)
                        continue;

                        counter+=countMoves(chessboard.createNextBoard(i, j, k, k2,board), depth-1,currentPlayerColor.getId()==1?Color.BLACK:Color.WHITE);
                    }
                }
            }
        }

        return counter;
    }
}
