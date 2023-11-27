package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.Arrays;
import java.util.Scanner;

public class ChessTest {
    
    private BoardManager boardManager;
    private Scanner scanner;

    public ChessTest(){
        boardManager=new BoardManager();
        boardManager.startNewMatch(5, boardManager.getDefaultStartConfig());
        scanner = new Scanner(System.in);
        playInConsole();
    }

    public void playInConsole(){

        ChessBoard board=boardManager.getManagedBoard();

        while (true) {

            System.out.println(board.toString());

            System.out.println(board.getTime()[0]+"  "+board.getTime()[1]);
            System.out.println(board.getTimeInMin()[0]+"  "+board.getTimeInMin()[1]);
            System.out.println("CurrentTurn: "+board.getCurrentActivePlyer());
            System.out.println("Piece to move:");
            String piece=scanner.nextLine();

            int[] pieceCH = translateFromChessNotation(piece);

            int[][] highlight=board.checkedGetHighlightOf(pieceCH[1], pieceCH[0]);

            boolean emptyFlag=true;

            for (int i = 0; i < highlight.length; i++) {
                for (int j = 0; j < highlight[i].length; j++) {
                    if(highlight[i][j]!=0){
                        emptyFlag=false;
                    }
                }
            }

            System.out.println(twoDArrtoString(highlight));

            if (emptyFlag) {
                continue;
            }


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

    public static void main(String[] args) {
        new ChessTest();
    }
}
