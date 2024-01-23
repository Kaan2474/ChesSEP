package com.ChesSEP.ChesSEP.ChessEngine;

public class ChessBot {

    private final double bauerValue=100;
    private final double springerValue=300;
    private final double laeuferValue=300;
    private final double turmValue=500;
    private final double koeniginValue=900;
    private final double koenigValue=300;

    private final double[][] springermulti=new double[][]{
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    private final double[][] koenigmulti=new double[][]{
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}
    };

    private final double[][] laeufermulti=new double[][]{
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}
    };

    private final double[][] damemulti = new double[][]{
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    private final double[][] bauermulti=new double[][]{
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}
    };

    private final double[][] turmmulti=new double[][]{
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}
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

        int[] score=null;

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

                    if(score==null){
                        score=new int[]{localDiference,i,j,moveBoard[i][j].validMoves.get(j2)[0], moveBoard[i][j].validMoves.get(j2)[1]};
                    }else if(score[0]-localDiference>0){
                        score=new int[]{localDiference,i,j,moveBoard[i][j].validMoves.get(j2)[0], moveBoard[i][j].validMoves.get(j2)[1]};
                    }   
                }
            }
        }

        return score;
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
                            scoreW+=bauerValue+bauermulti[i][j];
                        }else{
                            scoreB+=bauerValue+bauermulti[7-i][7-j];
                        }
                        break;
                    case SPRINGER:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=springerValue+springermulti[i][j];
                        }else{
                            scoreB+=springerValue+springermulti[7-i][7-j];
                        }
                        break;
                    case LAUFER:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=laeuferValue+laeufermulti[i][j];
                        }else{
                            scoreB+=laeuferValue+laeufermulti[7-i][7-j];
                        }
                        break;
                    case TURM:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=turmValue+turmmulti[i][j];
                        }else{
                            scoreB+=turmValue+turmmulti[7-i][7-j];
                        }
                        break;
                    case KOENIGIN:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=koeniginValue+damemulti[i][j];
                        }else{
                            scoreB+=koeniginValue+damemulti[7-i][7-j];
                        }
                        break;
                    case KOENIG:
                        if(testBoard[i][j].getColor()==Color.WHITE){
                            scoreW+=koenigValue+koenigmulti[i][j];
                        }else{
                            scoreB+=koenigValue+koenigmulti[7-i][7-j];
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
