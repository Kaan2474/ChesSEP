package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.List;

public class BoardManager {

    private ChessBoard Board;

    public void startNewMatch(double time,int[][][] startConfig){
        Board = new ChessBoard(time, startConfig);
    }

    public void startNewChessPuzzle(int[] status, int[][][] startConfig,int[][] moves){
        Board = new ChessBoard(status, startConfig, moves);
    }

    public void startNewTest (String FEN){
        Board = new ChessBoard(FEN);
    }

    public int[][][] getDefaultStartConfig(){
        int[][][] defaultBoard={
            {{2,2},{3,2},{4,2},{5,2},{6,2},{4,2},{3,2},{2,2}},
            {{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1}},
            {{2,1},{3,1},{4,1},{5,1},{6,1},{4,1},{3,1},{2,1}},
        };

        return defaultBoard;
    }

    public String getDefaultFEN(){
        return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    }

    public int[][][] getOnlyMatchStatus(){
        ChessBoard board=getManagedBoard();

        int[][] status= {{board.getZugId(),1,2,3,4,5,6},  //ZugID PosOfBoard PosOfColor PosOfKingIFAttacked BauerTransformEvent LetzterZug PosOfHighlightStatus
                        {(int)board.getCurrentTime(Color.WHITE),(int)board.getCurrentTime(Color.BLACK)},  //WhiteTime  BlackTime both in ms
                        {board.getWinner()},// 0 keiner , 1 weis, 2 schwarz, 3 unentschieden
                        {board.isPuzzle()}};// is Puzzle 1=ja 0=nein

        return new int[][][]{status};
    }


    public int[][][] getMatchFrame(Color color){
        ChessBoard board=getManagedBoard();

        List<int[][]> frame = new ArrayList<int[][]>();

        //Status Array
        int[][] status= {{board.getZugId(),1,2,3,4,5,6},  //ZugID PosOfBoard PosOfColor PosOfKingIFAttacked BauerTransformEvent LetzterZug PosOfHighlightStatus
                        {(int)board.getCurrentTime(Color.WHITE),(int)board.getCurrentTime(Color.BLACK)},  //WhiteTime  BlackTime both in ms
                        {board.getWinner()},// 0 keiner , 1 weis, 2 schwarz, 3 unentschieden
                        {board.isPuzzle()}};// is Puzzle 1=ja 0=nein

        frame.add(status);

        //Board
        frame.add(board.translateBoard(board.chessBoard,color));

        //Color
        frame.add(board.translateColorBoard(board.chessBoard,color));

        //schach
        frame.add(mergeArrays(board.getKingBoard(Color.WHITE,color), board.getKingBoard(Color.BLACK,color)));

        //BauerTransformEvent
        frame.add(board.getBauerTransformEvent(color));

        //LetzterZug    1=from 2=to
        frame.add(board.getLastMove(color)); 

        //Hightlights
        int counter=0;

        frame.add(new int[8][8]);

        int headerLength=frame.size();

        for (int i = 0; i < frame.get(frame.size()-1).length; i++) {
            for (int j = 0; j < frame.get(frame.size()-1)[i].length; j++) {
                if(frame.get(2)[i][j]==color.getId()){
                    int[][] arr;
                    if(color==Color.BLACK){
                        arr=board.checkedGetHighlightOf(7-i, 7-j,color);
                    }else{
                        arr=board.checkedGetHighlightOf(i, j,color);
                    }

                    if(!isTwoDArrayEmpty(arr)){
                        frame.add(arr);

                        //HiglightStatus
                        frame.get(headerLength-1)[i][j]=headerLength+counter;

                        counter++;
                    }
                }
            }
        }

        int[][][] frameArr;

        frameArr=new int[frame.size()][8][8];


        for (int i = 0; i < frame.size(); i++) {
            frameArr[i]=frame.get(i);
        }

        frame=null;
        
        return frameArr;
    }

    public ChessBoard getManagedBoard(){
        return Board;
    }

    public String twoDArrtoString(int[][] target){
        String result="";

        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                result+=" "+target[i][j]+" ";
            }
            result+="\n";
        }

        return result;
    }

    public boolean isTwoDArrayEmpty(int[][] arr){
        if(arr==null)
            return true;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(arr[i][j]!=0)
                    return false;
            }
        }

        return true;
    }

    public int[][] mergeArrays(int[][] arr1,int[][] arr2){
        int[][] resultArr=new int[8][8];
        for (int i = 0; i < arr2.length; i++) {
            for (int j = 0; j < arr2[i].length; j++) {
                if(arr1[i][j]!=0)
                    resultArr[i][j]=arr1[i][j];

                if(arr2[i][j]!=0)
                    resultArr[i][j]=arr2[i][j];
            }
        }

        return resultArr;
    }



    //Streaming

    public int[][][] streamingBoard(Color color){

        ChessBoard board=getManagedBoard();

        List<int[][]> frame = new ArrayList<int[][]>();

        //Status Array
        int[][] status= {{board.getZugId(),1,2,3,4,5,6},  //ZugID PosOfBoard PosOfColor PosOfKingIFAttacked BauerTransformEvent LetzterZug PosOfHighlightStatus
                {(int)board.getCurrentTime(Color.WHITE),(int)board.getCurrentTime(Color.BLACK)},  //WhiteTime  BlackTime both in ms
                {board.getWinner()},// 0 keiner , 1 weis, 2 schwarz, 3 unentschieden
                {board.isPuzzle()}};// is Puzzle 1=ja 0=nein

        frame.add(status);

        //Board
        frame.add(board.translateBoard(board.chessBoard,color));

        //Color
        frame.add(board.translateColorBoard(board.chessBoard,color));

        //schach
        frame.add(mergeArrays(board.getKingBoard(Color.WHITE,color), board.getKingBoard(Color.BLACK,color)));

        //BauerTransformEvent
        frame.add(board.getBauerTransformEvent(color));

        //LetzterZug    1=from 2=to
        frame.add(board.getLastMove(color));

        int[][][] frameArr;

        frameArr=new int[frame.size()][8][8];


        for (int i = 0; i < frame.size(); i++) {
            frameArr[i]=frame.get(i);
        }

        frame=null;

        return frameArr;

    }

    public static void main(String[] args) {
        BoardManager boardManager = new BoardManager();
        boardManager.startNewMatch(5, boardManager.getDefaultStartConfig());
        //boardManager.getManagedBoard().toggleCurrentPlayer();
        int[][][] frame= boardManager.getMatchFrame(Color.BLACK);

        for (int i = 0; i < frame.length; i++) {
            System.out.println(i+":");

                System.out.println(boardManager.twoDArrtoString(frame[i]));
            
        }

        //boardManager.getManagedBoard().toggleCurrentPlayer();
        //System.out.println(boardManager.twoDArrtoString(boardManager.getManagedBoard().getHighlightOf(7, 0)));
        
    }
}
