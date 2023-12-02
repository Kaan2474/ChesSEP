package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.List;

public class BoardManager {

    private ChessBoard Board;

    public BoardManager(){

    }

    public void startNewMatch(double time,int[][][] startConfig){
        Board = new ChessBoard(time, startConfig);
    }

    public int[][][] getDefaultStartConfig(){
        int[][][] defaultBoard={
            {{2,1},{3,1},{4,1},{5,1},{6,1},{4,1},{3,1},{2,1}},
            {{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
            {{2,2},{3,2},{4,2},{5,2},{6,2},{4,2},{3,2},{2,2}},
        };

        return defaultBoard;
    }

    public int[][][] getEndGameTestBoard(){
        int[][][] Board={
            {{0,0},{0,0},{0,0},{1,1},{0,0},{1,1},{0,0},{0,0}},
            {{1,1},{0,0},{0,0},{1,1},{6,1},{1,1},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{4,1},{0,0},{1,1},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{2,2},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{6,2},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
        };

        return Board;
    }

    public int[][][] getBauerTestBoard(){
        int[][][] Board={
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{1,1},{0,0},{0,0},{0,0},{0,0},{1,2},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{1,2},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{1,1},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,2}},
        };

        return Board;
    }

    //TODO mach das board
    public int[][][] getRohadeTestBoard(){
        int[][][] Board={
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{1,1},{0,0},{0,0},{0,0},{0,0},{1,2},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{1,2},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{1,1},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,2}},
        };

        return Board;
    }

    public int[][][] getMatchFrame(Color color){
        ChessBoard board=getManagedBoard();

        List<int[][]> frame = new ArrayList<int[][]>();

        //Status Array
        int[][] status= {{board.getZugId(),1,2,3,4,5,6},  //ZugID PosOfBoard PosOfColor PosOfKingIFAttacked BauerTransformEvent LetzterZug PosOfHighlightStatus
                        {(int)board.getTimeLong(color),(int)board.getTimeLong(color)},  //WhiteTime  BlackTime both in ms
                        {0}}; 

        if(board.getWinner()!=null){
            status[2][0]=board.getWinner().getId();
        }
                        

        frame.add(status);

        //Board
        frame.add(board.translateBoard());

        //Color
        frame.add(board.translateColorBoard());

        //KingEvent
        frame.add(mergeArrays(board.getKingBoard(Color.WHITE), board.getKingBoard(Color.BLACK)));

        //BauerTransformEvent
        frame.add(board.getBauerTransformEvent());

        //LetzterZug    1=from 2=to
        frame.add(board.getLastMove()); 

        //Hightlights
        int counter=0;

        frame.add(new int[8][8]);

        int headerLength=frame.size();

        for (int i = 0; i < frame.get(0).length; i++) {
            for (int j = 0; j < frame.get(1)[i].length; j++) {
                if(frame.get(2)[i][j]==color.getId()){
                    int[][] arr=board.checkedGetHighlightOf(i, j);

                    if(!isTwoDArrayEmpty(arr)){
                        frame.add(board.checkedGetHighlightOf(i, j));

                        //HiglightStatus
                        frame.get(headerLength-1)[i][j]=5+counter;

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

    public static void main(String[] args) {
        BoardManager boardManager = new BoardManager();
        boardManager.startNewMatch(5, boardManager.getDefaultStartConfig());
        //boardManager.getManagedBoard().toggleCurrentPlayer();
        int[][][] frame= boardManager.getMatchFrame(Color.WHITE);

        for (int i = 0; i < frame.length; i++) {
            System.out.println(i+":");

                System.out.println(boardManager.twoDArrtoString(frame[i]));
            
        }

        //boardManager.getManagedBoard().toggleCurrentPlayer();
        //System.out.println(boardManager.twoDArrtoString(boardManager.getManagedBoard().getHighlightOf(7, 0)));
        
    }
}
