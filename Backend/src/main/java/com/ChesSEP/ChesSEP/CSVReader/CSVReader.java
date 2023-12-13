package com.ChesSEP.ChesSEP.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public String[] splitStringIntoPuzzles(String fileContent) throws IOException{
        List<String> puzzles=new ArrayList<>();

        BufferedReader reader= new BufferedReader(new StringReader(fileContent));

        String line=reader.readLine();

        while (line!=null) {
            puzzles.add(line);
            line=reader.readLine();
        }

        String[] result=new String[puzzles.size()];

        for (int i = 0; i < result.length; i++) {
            result[i]=puzzles.get(i);
        }

        reader.close();

        return result;
    }

    public int[][][] CSVtoBoard(int puzzleNr,String filePath) throws IOException{
        return CSVtoBoard(puzzleNr, importFile(filePath));
    }

    public int[][][] CSVtoBoard(int puzzleNr,String[] fileContent){
        int FENpos = 0;
        String FEN;

        int[][][] resultArr = new int[8][8][2];

        String[] fileStatus=fileContent[0].split(",");

        for (int i = 0; i < fileStatus.length; i++) {
            if("FEN".equals(fileStatus[i])){
                FENpos = i;
                break;
            }
        }

        FEN = fileContent[puzzleNr].split(",")[FENpos];

        String[] currentLines=FEN.split("/|\s");

        for(int i = 0; i < 8; i++){

            String currentLine=currentLines[i];
            int offset=0;
            for (int j = 0; j < currentLine.length(); j++) {
                String currentChar=""+currentLine.charAt(j);

                if(currentChar.matches("[1-8]")){
                    for (int k = 0; k < Integer.parseInt(currentChar); k++) {
                        offset++;
                    }
                    offset--;
                    continue;
                }
                
                resultArr[i][j+offset]=translateToIdNotation(currentChar.charAt(0));
            }
        }


        return resultArr;
    }

    public int[][] MovesToArr(int puzzleNr,String filePath) throws IOException{
        return MovesToArr(puzzleNr, importFile(filePath));
    }

    public int[][] MovesToArr(int puzzleNr,String[] fileContent){
        int movesPos = 0;
        String moves;

        String[] fileStatus=fileContent[0].split(",");

        for (int i = 0; i < fileStatus.length; i++) {
            if("Moves".equals(fileStatus[i])){
                movesPos = i;
                break;
            }
        }

        moves = fileContent[puzzleNr].split(",")[movesPos];

        String[] currentCoords=moves.split("\s");
        int[][] result=new int[currentCoords.length][2];

        for (int i = 0; i < currentCoords.length; i++) {
            result[i]=new int[]{translateCoord(currentCoords[i].substring(0, 2)),translateCoord(currentCoords[i].substring(2))};
        }

        return result;
    }

    public int[] getStatus(int puzzleNr,String filePath) throws IOException{
        return getStatus(puzzleNr, importFile(filePath));
    }

    public int[] getStatus(int puzzleNr,String[] fileContent){
        int statusPos = 0;
        String status;

        int[] resultArr = new int[6];

        String[] fileStatus=fileContent[0].split(",");

        for (int i = 0; i < fileStatus.length; i++) {
            if("FEN".equals(fileStatus[i])){
                statusPos = i;
                break;
            }
        }

        status=fileContent[puzzleNr].split(",")[statusPos];

        String[] statusArr=status.split("\s");

        int[] castlingAvailability=parseCastlingAvailability(statusArr[2]);

        resultArr=new int[]{
            translateColorToId(statusArr[1]),   //ActiveColor  
            castlingAvailability[0],    //Castling Availability Q
            castlingAvailability[1],    //K
            castlingAvailability[2],    //q
            castlingAvailability[3],    //k
            translateCoord(statusArr[3])};   //En Passnat TargetSquare

        return resultArr;
    }

    private int[] parseCastlingAvailability(String castlingAvailability){
        int[] result = new int[4];
        if(castlingAvailability.contains("Q"))
            result[0]=1;

        if(castlingAvailability.contains("K"))
            result[1]=1;

        if(castlingAvailability.contains("q"))
            result[2]=1;

        if(castlingAvailability.contains("k"))
            result[3]=1;

        return result;
    }

    private int translateColorToId(String x){
        if(x.equals("w")){
            return 1;
        }

        return 2;
    }

    private int translateCoord(String x){
        int result=0;

        switch (x.charAt(0)) {
            case 'a':
                result=0;
                break;
            case 'b':
                result=1;
                break;
            case 'c':
                result=2;
                break;
            case 'd':
                result=3;
                break;
            case 'e':
                result=4;
                break;
            case 'f':
                result=5;
                break;
            case 'g':
                result=6;
                break;
            case 'h':
                result=7;
                break;
            default:
                return -1;
        }

        result=result*10;
        result=result+(7-(Integer.parseInt(""+x.charAt(1))-1));

        return result;
    }

    

    public int[] translateToIdNotation(char piece){
        String stPiece=""+piece;
        int[] result=new int[2];

        if(stPiece.toLowerCase()==stPiece){
            result[1]=2;
        }else{
            result[1]=1;
        }

        stPiece=stPiece.toLowerCase();

        switch (stPiece) {
            case "k":
                result[0] = 6;
                break;

            case "q":
                result[0] = 5;
                break;

            case "r":
                result[0] = 2;
                break;

            case "b":
                result[0] = 4;
                break;

            case "n":
                result[0] = 3;
                break;

            case "p":
                result[0] = 1;
                break;
            default:
                result[0]=0;
                result[1]=0;
                break;
        }

        return result;
    }

    public String[] getPuzzleInfo(String filePath) throws IOException{
        return getPuzzleInfo(importFile(filePath));
    }

    public String[] getPuzzleInfo(String[] fileContent){

        String[] fileStatus=fileContent[0].split(",");
        int infoPos=0;

        for (int i = 0; i < fileContent.length; i++) {
            if("Themes".equals(fileStatus[i])){
                infoPos = i;
                break;
            }
        }

        String[] result=new String[fileContent.length-1];

        for (int i = 0; i < result.length; i++) {
            String[] currentPuzzle=fileContent[i+1].split(",");

            result[i]=currentPuzzle[infoPos];
        }

        return result;
    }

    private String[] importFile(String csvFile) throws IOException{
        List<String> fileContent=new ArrayList<>();
        String line;

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        while ((line=br.readLine())!=null) {
            fileContent.add(line);
        }

        br.close();

        String[] contentArr=new String[fileContent.size()];

        for (int i = 0; i < fileContent.size(); i++) {
            contentArr[i]=fileContent.get(i);
        }

        return contentArr;
    }


    public static void main(String[] args) {
        CSVReader reader = new CSVReader();
        int[][][] threeDArray=new int[][][]{{{0}}};
        int puzzleNr=1;

        try {
            threeDArray = reader.CSVtoBoard(puzzleNr,"C:\\Users\\jonas\\Downloads\\chess_puzzles.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        String result="";
        for (int i = 0; i < threeDArray.length; i++) {
            for (int j = 0; j < threeDArray[i].length; j++) {
                result+=" ";
                for (int k = 0; k < threeDArray[i][j].length; k++) {
                    result+=threeDArray[i][j][k];
                }
                result+=" ";
            }
            result+="\n";
        }

        System.out.println(result);
        
        int[][] moves=new int[][]{{0}};

        try {
            moves=reader.MovesToArr(puzzleNr,"C:\\Users\\jonas\\Downloads\\chess_puzzles.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        result="";

        for (int i = 0; i < moves.length; i++) {
            result+=moves[i][0]+" "+moves[i][1]+"\n";
        }

        System.out.println(result);

        int[] status=new int[]{0};

        try {
            status=reader.getStatus(puzzleNr, "C:\\Users\\jonas\\Downloads\\chess_puzzles.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }

        result="";

        for (int i = 0; i < status.length; i++) {
            result+=status[i]+" ";
        }
        
        System.out.println(result);

        String[] info=new String[]{""};

        try {
            info=reader.getPuzzleInfo("C:\\Users\\jonas\\Downloads\\chess_puzzles.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }

        result="";

        for (int i = 0; i < info.length; i++) {
            result+=info[i]+" \n";
        }
        
        System.out.println(result);
    }

}
