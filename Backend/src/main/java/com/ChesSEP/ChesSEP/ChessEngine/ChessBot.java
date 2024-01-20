package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessBot {

    private final double bauerValue=100;
    private final double springerValue=300;
    private final double laeuferValue=300;
    private final double turmValue=500;
    private final double koeniginValue=900;
    private final double koenigValue=300;

    private final double[][] springermulti=new double[][]{
        {0.5,0.65,0.75,0.75,0.75,0.75,0.65,0.5},
        {0.65,0.75,1,1,1,1,0.75,0.65},
        {0.65,1,1,1,1,1,1,0.65},
        {0.65,1,1,1,1,1,1,0.65},
        {0.65,1,1,1,1,1,1,0.65},
        {0.65,1,1,1,1,1,1,0.65},
        {0.65,0.75,1,1,1,1,0.75,0.65},
        {0.5,0.65,0.75,0.75,0.75,0.75,0.65,0.5},
    };

    private final double[][] koenigmulti=new double[][]{
        {0.75,0.65,0.65,0.5,0.5,0.65,0.65,0.75},
        {0.75,0.65,0.65,0.5,0.5,0.65,0.65,0.75},
        {0.75,0.65,0.65,0.5,0.5,0.65,0.65,0.75},
        {0.65,0.5,0.5,0.5,0.5,0.5,0.5,0.65},
        {0.65,0.5,0.5,0.5,0.5,0.5,0.5,0.65},
        {0.75,0.65,0.65,0.5,0.5,0.65,0.65,0.75},
        {0.75,0.65,0.65,0.5,0.5,0.65,0.65,0.75},
        {0.75,0.65,0.65,0.5,0.5,0.65,0.65,0.75},
    };

    private final double[][] laeufermulti=new double[][]{
        {0.5,0.65,0.65,0.65,0.65,0.65,0.65,0.5},
        {0.65,0.75,0.75,0.75,0.75,0.75,0.75,0.65},
        {0.65,0.75,1,1,1,1,0.75,0.65},
        {0.65,0.75,1,1,1,1,0.75,0.65},
        {0.65,0.75,1,1,1,1,0.75,0.65},
        {0.65,0.75,1,1,1,1,0.75,0.65},
        {0.65,0.75,0.75,0.75,0.75,0.75,0.75,0.65},
        {0.5,0.65,0.65,0.65,0.65,0.65,0.65,0.5}
    };

    private final double[][] damemulti = new double[][]{
        {0.65,0.75,0.75,0.75,0.75,0.75,0.75,0.65},
        {0.75,0.85,0.85,0.85,0.85,0.85,0.85,0.75},
        {0.75,0.85,1,1,1,1,0.85,0.75},
        {0.75,0.85,1,1,1,1,0.85,0.75},
        {0.75,0.85,1,1,1,1,0.85,0.75},
        {0.75,0.85,1,1,1,1,0.85,0.75},
        {0.75,0.85,0.85,0.85,0.85,0.85,0.85,0.75},
        {0.65,0.75,0.75,0.75,0.75,0.75,0.75,0.65}
        };

    private final double[][] bauermulti=new double[][]{
        {1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1},
        {0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75},
        {0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75},
        {0.65,0.65,0.65,0.65,0.65,0.65,0.65,0.65},
        {0.65,0.5,0.5,0.65,0.65,0.5,0.5,0.65},
        {0.65,0.65,0.65,0.5,0.5,0.65,0.65,0.65},
        {0.65,0.65,0.65,0.65,0.65,0.65,0.65,0.65}
    };

    private final double[][] turmmulti=new double[][]{
        {0.65,0.65,0.65,0.65,0.65,0.65,0.65,0.65},
        {0.65,1,1,1,1,1,1,0.65},
        {0.5,0.65,0.65,0.65,0.65,0.65,0.65,0.5},
        {0.5,0.65,0.65,0.65,0.65,0.65,0.65,0.5},
        {0.5,0.65,0.65,0.65,0.65,0.65,0.65,0.5},
        {0.5,0.65,0.65,0.65,0.65,0.65,0.65,0.5},
        {0.65,1,1,1,1,1,1,0.65},
        {0.65,0.65,0.65,0.65,0.65,0.65,0.65,0.65}
    };

    ChessBoard moveGen;
    Color playerColor;
    
    public ChessBot(){
        moveGen=new ChessBoard();
    }

    public int[] getBestMove(ChessPiece[][] initialBoard,Color playerColor,difficulty difficulty){

        return generateNextBestMove(initialBoard, playerColor, difficulty.getDepth());
    }

    private int[] generateNextBestMove(ChessPiece[][] initialBoard,Color playerColor,int depth){

        if(depth==0)
            return boardScorer(initialBoard);
            

        ChessPiece[][] moveBoard=moveGen.getAllCoords(initialBoard, playerColor);

        List<int[]> scoreList=new ArrayList<>();

        for (int i = 0; i < moveBoard.length; i++) {
            for (int j = 0; j < moveBoard[i].length; j++) {
                if(moveBoard[i][j]==null)
                    continue;

                for (int j2 = 0; j2 < moveBoard[i][j].validMoves.size(); j2++) {
                    int[] scoreOfMove=generateNextBestMove(moveGen.createNextBoard(i, j, moveBoard[i][j].validMoves.get(j2)[0], moveBoard[i][j].validMoves.get(j2)[1], initialBoard), 
                        playerColor==Color.WHITE?Color.BLACK:Color.WHITE, 
                        depth-1);

                    int localDiference;

                    if(playerColor==Color.WHITE){
                        localDiference=scoreOfMove[0]-scoreOfMove[1];
                    }else{
                        localDiference=scoreOfMove[1]-scoreOfMove[0];
                    }

                    scoreList.add(new int[]{localDiference,i,j,moveBoard[i][j].validMoves.get(j2)[0], moveBoard[i][j].validMoves.get(j2)[1]});
                }
            }
        }

        return Collections.min(scoreList,(x,y)->x[0]-y[0]);
    }

    private int[] boardScorer(ChessPiece[][] testBoard){
        double scoreW=0;
        double scoreB=0;

        for (int i = 0; i < testBoard.length; i++) {
            for (int j = 0; j < testBoard[i].length; j++) {
                if(testBoard[i][j]==null)
                    continue;

                switch (testBoard[i][j].getType()) {
                    case BAUER:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=bauerValue*bauermulti[i][j];
                        }else{
                            scoreB+=bauerValue*bauermulti[7-i][j];
                        }
                        break;
                    case SPRINGER:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=springerValue*springermulti[i][j];
                        }else{
                            scoreB+=springerValue*springermulti[i][j];
                        }
                        break;
                    case LAUFER:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=laeuferValue*laeufermulti[i][j];
                        }else{
                            scoreB+=laeuferValue*laeufermulti[i][j];
                        }
                        break;
                    case TURM:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=turmValue*turmmulti[i][j];
                        }else{
                            scoreB+=turmValue*turmmulti[i][j];
                        }
                        break;
                    case KOENIGIN:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=koeniginValue*damemulti[i][j];
                        }else{
                            scoreB+=koeniginValue*damemulti[i][j];
                        }
                        break;
                    case KOENIG:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=koenigValue*koenigmulti[i][j];
                        }else{
                            scoreB+=koenigValue*koenigmulti[i][j];
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return new int[]{(int)scoreW,(int)scoreB};
    }




}
