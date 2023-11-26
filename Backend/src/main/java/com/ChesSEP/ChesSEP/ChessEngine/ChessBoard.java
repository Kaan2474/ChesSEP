package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private ChessPiece[][] Board;

    private List<ChessOperation> Zuege;

    private List<Integer[]> validCoords;

    private Color currentPlayer;

    private int amountOfpieces;

    private Long whiteTime;
    private Long blackTime;

    private Long intervallStart;

    private final int SpringerOffset[][]={{1,2},{-1,2},{1,-2},{-1,-2},{2,1},{-2,1},{2,-1},{-2,-1}};
    private final int KönigOffset[][]={{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};

    public ChessBoard(Long time,int[][][] Board){
        this.Board=constructBoard(Board);
        currentPlayer=Color.WHITE;
        whiteTime=time;
        blackTime=time;
        amountOfpieces=32;
        Zuege=new ArrayList<ChessOperation>();
    }

    public ChessPiece[][] constructBoard(int[][][] Board){
       
        ChessPiece[][] ChessBoard=new ChessPiece[8][8];

        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {

                if(Board[i][j][0]!=0){
                    ChessBoard[i][j]=new ChessPiece(Board[i][j][0], Board[i][j][1]);
                }else{
                    ChessBoard[i][j]=null;
                }
            }
        }
        
        return ChessBoard;
    }


    public void nextStep(int x, int y, int gotoX, int gotoY){
        if(!movePiece(x, y, gotoX, gotoY)){
            return;
        }

        /*if(intervallStart!=0){
            Long currentTime;

            if(currentPlayer==Color.WHITE){
                currentTime=whiteTime;
            }else{
                currentTime=blackTime;
            }

            currentTime=currentTime
        }
        intervallStart=System.currentTimeMillis();*/

        toggleCurrentPlayer();

    }

    private void toggleCurrentPlayer(){
        if(currentPlayer==Color.WHITE){
            currentPlayer=Color.BLACK;
        }else{
            currentPlayer=Color.WHITE;
        }
    }

    public Color getCurrentActivePlyer(){
        return currentPlayer;
    }

    private boolean movePiece(int x, int y, int gotoX, int gotoY){
        
        if(!isPieceOn(x, y))
        return false;

        if(!moveIsValid(x, y, gotoX, gotoY))
        return false; 
        

        ChessPiece currentPiece=getPieceOn(x, y);
        deletePieceOn(x, y);
        System.out.println(currentPiece.toString());
        ChessPiece previousPiece=putPieceOn(currentPiece, gotoX, gotoY);

        Zuege.add(new ChessOperation(x, y, gotoX, gotoY, currentPiece, previousPiece));

        return true;
    }

    private Boolean moveIsValid(int x, int y, int gotoX, int gotoY){
        if(getPieceOn(x, y).getColor()!=currentPlayer)
        return false;

        ValidCoordsOf(x,y);

        if(!CoordValid(gotoX, gotoY))
        return false;  

        return true;
    }

    private boolean CoordValid(int x, int y){
        for (int i = 0; i < validCoords.size(); i++) {
            if(validCoords.get(i)[0]==x&&validCoords.get(i)[1]==y)
            return true;
        }

        return false;
    }

    public int getCurrentAmountOfPieces(){
        return this.amountOfpieces;
    }

    public int[][] getHighlightOf(int x, int y){
        ValidCoordsOf(x, y);
        int[][] result=new int[8][8];

        for (int i = 0; i < validCoords.size(); i++) {
            result[validCoords.get(i)[0]][validCoords.get(i)[1]]=1;
        }
        
        return result;
    }

    public int[][] checkedGetHighlightOf(int x, int y){
        int[][] result=new int[8][8];

        if(!isPieceOn(x, y, currentPlayer)){
            return result;
        }

        ValidCoordsOf(x, y);

        for (int i = 0; i < validCoords.size(); i++) {
            result[validCoords.get(i)[0]][validCoords.get(i)[1]]=1;
        }
        
        return result;
    }

    private boolean isInBounds(int x, int y){
        if((x>=8||x<=-1)||(y>=8||y<=-1)){
            return false;
        }

        return true;
    }

    private void ValidCoordsOf(int x,int y){
        validCoords=new ArrayList<Integer[]>();
        
        System.out.println(getPieceOn(x, y).getType().name());

        switch (getPieceOn(x, y).getType()) {
            case BAUER:
                getValidBauerCoords(x, y);
                break;
                        
            case TURM:

                getValidHorizontalCoords(x, y);   
                getValidVerticalCoords(x, y);
                break;

            case SPRINGER:
                getValidOffsetCoords(x, y, SpringerOffset);
                break;

            case LAUFER_W:
                
                getValidDiagonalCoords(x, y);
                break;

            case KOENIGIN:

                getValidDiagonalCoords(x, y);
                getValidHorizontalCoords(x, y);  
                getValidVerticalCoords(x, y);
                break;

            case KOENIG:
                getValidOffsetCoords(x, y, KönigOffset);     
                break;

            case LAUFER_S:
                
                getValidDiagonalCoords(x, y);    
                break;
        }
    }

    private boolean checkCoord(int x,int y){
        if(getPieceOn(x, y)==null){
                validCoords.add(new Integer[]{x,y});
            return true;
        }

        if(getPieceOn(x, y).getColor()!=currentPlayer){
            validCoords.add(new Integer[]{x,y});
            return false;
        }
            
        return false;
    }

    private void getValidOffsetCoords(int x,int y,int[][] offset){
        int currentx;
        int currenty;

        for (int i = 0; i < offset.length; i++) {
            currentx=x+offset[i][0];
            currenty=y+offset[i][1];

            if((currentx>=8||currentx<=-1)||(currenty>=8||currenty<=-1))
                continue;

            if(!isPieceOn(currentx, currenty, currentPlayer))
                validCoords.add(new Integer[]{currentx,currenty});

        }
    }

    private void getValidBauerCoords(int x, int y){
        ChessPiece piece=getPieceOn(x, y);

        switch (piece.getColor()) {
            case WHITE:
                if(!isPieceOn(x+1,y,Color.WHITE)){
                    validCoords.add(new Integer[]{x+1,y});
                }

                if(!piece.gethasMoved()&&isInBounds(x, y)){
                    validCoords.add(new Integer[]{x+2,y});
                }

                if(isPieceOn(x+1, y+1, Color.BLACK)){
                    validCoords.add(new Integer[]{x+1,y+1});
                }

                if(isPieceOn(x+1, y-1, Color.BLACK)){
                    validCoords.add(new Integer[]{x+1,y-1});
                }
            break;

            case BLACK:

                if(!isPieceOn(x-1,y,Color.BLACK)){
                    validCoords.add(new Integer[]{x-1,y});
                }

                if(!piece.gethasMoved()&&isInBounds(x, y)){
                    validCoords.add(new Integer[]{x-2,y});
                }

                if(isPieceOn(x-1, y+1, Color.WHITE)){
                    validCoords.add(new Integer[]{x-1,y+1});
                }

                if(isPieceOn(x-1, y-1, Color.WHITE)){
                    validCoords.add(new Integer[]{x-1,y-1});
                }
            break;
        }
    }

    private void getValidDiagonalCoords(int x, int y){
        getValidNWDiagonalCoords(x, y);
        getValidNODiagonalCoords(x, y);
        getValidSODiagonalCoords(x, y);
        getValidSWDiagonalCoords(x, y);
    }

    private void getValidSWDiagonalCoords(int x,int y){
        y--;
        x--;

        while (isInBounds(x, y)) {
            if(!checkCoord(x, y))
            return;

            y--;
            x--;
        }
    }

    private void getValidSODiagonalCoords(int x,int y){
        y--;
        x++;

        while (isInBounds(x, y)) {
            if(!checkCoord(x, y))
            return;

            y--;
            x++;
        }
    }

    private void getValidNODiagonalCoords(int x,int y){
        y++;
        x++;

        while (isInBounds(x, y)) {
            if(!checkCoord(x, y))
            return;

            y++;
            x++;
        }
    }

    private void getValidNWDiagonalCoords(int x,int y){
        y++;
        x--;

        while (isInBounds(x, y)) {
            if(!checkCoord(x, y))
            return;

            y++;
            x--;
        }
    }

    private void getValidVerticalCoords(int x,int y){
        getValidPositiveVerticalCoords(x, y);
        getValidNegativeVerticalCoords(x, y);
    }

    private void getValidPositiveVerticalCoords(int x,int y){
        do{
            if(!checkCoord(x, y))
            return;

            y++;

        }while (isInBounds(x, y));
    }


    private void getValidNegativeVerticalCoords(int x,int y){
        do{
            if(!checkCoord(x, y))
            return;

            y--;
        }while (isInBounds(x, y));
    }

    private void getValidHorizontalCoords(int x,int y){
        getValidPositiveHorizontalCoords(x, y);
        getValidNegativeHorizontalCoords(x, y);
    }

    private void getValidPositiveHorizontalCoords(int x,int y){
        do{
            if(!checkCoord(x, y))
            return;

            x++;
        }while (isInBounds(x, y));
    }

    private void getValidNegativeHorizontalCoords(int x,int y){
        do{
            if(!checkCoord(x, y))
            return;

            x--;
        }while (isInBounds(x, y));
    }

    private ChessPiece getPieceOn(int x,int y){
        if(!isInBounds(x, y))
        return null;

        return Board[x][y];
    }

    private Boolean isPieceOn(int x,int y,Color color){

        if(!isInBounds(x, y))
        return false;

        if(getPieceOn(x, y)==null)
        return false;

        return getPieceOn(x, y).getColor()==color;
    }

    private Boolean isPieceOn(int x,int y){
        return Board[x][y]!=null;
    }

    private void deletePieceOn(int x,int y){
        Board[x][y]=null;
    }

    private ChessPiece putPieceOn(ChessPiece piece,int x,int y){
        ChessPiece previousPiece=getPieceOn(x, y);

        if(previousPiece!=null)
        amountOfpieces--;

        piece.sethasMovedTrue();

        Board[x][y]=piece;

        return previousPiece;  
    }

    public String pieceToString(int x, int y){
        return getPieceOn(x, y).toString();
    }

    @Override
    public String toString(){

        String result="";

        for (int i = 0; i < Board.length; i++) {
            result+=Integer.toString(i+1)+"  ";
            for (int j = 0; j < Board[i].length; j++) {
                if(getPieceOn(i, j)!=null){
                    result+="  ";
                    switch (getPieceOn(i, j).getType()) {
                        case BAUER:
                            result+="  "+ChessPieceType.BAUER.name()+" ";
                            break;
                        
                        case TURM:
                            result+="  "+ChessPieceType.TURM.name()+"  ";
                            break;

                        case SPRINGER:
                            result+=ChessPieceType.SPRINGER.name();
                            break;

                        case LAUFER_W:
                            result+=ChessPieceType.LAUFER_W.name();
                            break;

                        case KOENIGIN:
                            result+=ChessPieceType.KOENIGIN.name();
                            break;

                        case KOENIG:
                            result+=" "+ChessPieceType.KOENIG.name()+" ";
                            break;

                        case LAUFER_S:
                            result+=ChessPieceType.LAUFER_S.name();
                            break;
                    }
                    result+="  ";
                }else{
                    result+="    [  ]    ";
                }
                
            }
            result+=Integer.toString(i+1)+"  ";
            result+="\n";
        }
        return result;
    }
}
