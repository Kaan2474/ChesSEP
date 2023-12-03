package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.Arrays;
import java.util.Scanner;

public class ChessTest {
    
    private BoardManager boardManager;
    private Scanner scanner;

    public ChessTest(){
        boardManager=new BoardManager();
        boardManager.startNewMatch(5, getRemisTestBoard());
        scanner = new Scanner(System.in);
        playInConsole();
    }

    public void playInConsole(){

        ChessBoard board=boardManager.getManagedBoard();

        while (true) {

            if(board.hasBauerToTransform()){
                System.out.println("Change Bauer into:");
                String piece=scanner.nextLine();

                board.transformBauer(Integer.parseInt(piece));
                continue;
            }

            System.out.println(board.toString());
            System.out.println("Winner:"+board.getWinner());
            System.out.println("Time: "+board.getTime(Color.WHITE)[0]+":"+board.getTime(Color.WHITE)[1]+"  "+"Time: "+board.getTime(Color.BLACK)[0]+":"+board.getTime(Color.BLACK)[1]);
            System.out.println("Time: "+board.getTimeLong(Color.WHITE)+"  Time: "+board.getTimeLong(Color.BLACK));
            System.out.println("CurrentTurn: "+board.getCurrentActivePlyer());
            System.out.println("Piece to move:");
            String piece=scanner.nextLine();

            int[] pieceCH = translateFromChessNotation(piece);

            int[][] highlight=board.checkedGetHighlightOf(pieceCH[1], pieceCH[0]);

            if (highlight==null) {
                continue;
            }

            System.out.println(twoDArrtoString(highlight));

            


            //System.out.println(board.pieceToString(pieceCH[1], pieceCH[0]));

            System.out.println("move to:");
            String location=scanner.nextLine();
            
            //System.out.println(piece+" "+location);

            
            int[] locationCH = translateFromChessNotation(location);

            System.out.println(Arrays.toString(pieceCH));
            System.out.println(Arrays.toString(locationCH));

            //System.out.println(boardManager.getManagedBoard().);

            if(pieceCH[0]!=-1&&locationCH[0]!=-1){
                boardManager.getManagedBoard().nextStep(pieceCH[1], pieceCH[0],locationCH[1], locationCH[0]);
            }
        }
    }

    public int[] translateFromChessNotation(String coord){

        int[] result={-1,Integer.parseInt(""+coord.charAt(1))-1};

        switch (coord.charAt(0)) {
            case 'A':
                result[0]=0;
                break;
            case 'B':
                result[0]=1;
                break;
            case 'C':
                result[0]=2;
                break;
            case 'D':
                result[0]=3;
                break;
            case 'E':
                result[0]=4;
                break;
            case 'F':
                result[0]=5;
                break;
            case 'G':
                result[0]=6;
                break;
            case 'H':
                result[0]=7;
                break;
            default:
                break;
        }

        return result;
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
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{1,2},{0,0},{0,0},{1,1},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{1,2}}
        };

        return Board;
    }

    public int[][][] getRohadeTestBoard(){
        int[][][] Board={
            {{2,1},{0,0},{0,0},{0,0},{6,1},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{2,1}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{2,2},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{6,2},{0,0},{0,0},{2,2}}
        };

        return Board;
    }

    public int[][][] getRemisTestBoard(){
        int[][][] defaultBoard={
            {{2,1},{0,0},{0,0},{0,0},{6,1},{4,1},{3,1},{2,1}},
            {{0,0},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}},
            {{0,0},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}},
            {{2,2},{3,2},{4,2},{5,2},{6,2},{4,2},{3,2},{2,2}},
        };

        return defaultBoard;
    }

    public static void main(String[] args) {
        new ChessTest();
    }
}
