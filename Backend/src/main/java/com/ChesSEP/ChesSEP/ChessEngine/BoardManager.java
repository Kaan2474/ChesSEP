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

    public int[][][] getMatchFrame(Color color){
        ChessBoard board=getManagedBoard();

        int[][][] frame = new int[5+board.getCurrentAmountOfPieces(color)][8][8];

        //Status Array
        int[][] status= {{board.getZugId(),1,2,3,4,5}};

        frame[0]=status;

        //Board
        frame[1]=board.translateBoard();

        //Color
        frame[2]=board.translateColorBoard();

        //Rohade
        //frame[3]=

        //Hightlights
        int counter=0;

        for (int i = 0; i < frame[1].length; i++) {
            for (int j = 0; j < frame[1][i].length; j++) {
                if(frame[2][i][j]==color.getId()){
                    frame[5+counter]=board.checkedGetHighlightOf(i, j);

                    //HiglightStatus
                    frame[4][i][j]=5+counter;

                    counter++;
                }
            }
        }

        return frame;
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
