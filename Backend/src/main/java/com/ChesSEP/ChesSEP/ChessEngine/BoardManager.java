package com.ChesSEP.ChesSEP.ChessEngine;

public class BoardManager {

    private ChessBoard Board;

    public BoardManager(){

    }

    public void startNewMatch(double time,int[][][] startConfig){
        Board = new ChessBoard(time, startConfig);
    }

    public int[][][] getDefaultStartConfig(){
        int[][][] defaultBoard={
            {{2,1},{3,1},{7,1},{5,1},{6,1},{4,1},{3,1},{2,1}},
            {{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
            {{2,2},{3,2},{4,2},{5,2},{6,2},{7,2},{3,2},{2,2}},
        };

        return defaultBoard;
    }

    //dummy
    public String convertBoardToString(int[][] Board){
        return"";
    }

    //dummy
    public int[][] convertStringToBoard(String Board){
        return new int[8][8]; 
    }

    public ChessBoard getManagedBoard(){
        return Board;
    }
}
