package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    public ChessPiece[][] Board;

    private List<ChessOperation> zuege;
    private int zugId;

    private Color currentPlayer;

    private int amountOfpieces;
    private int amountOfWhitepieces;
    private int amountOfBlackpieces;

    private long whiteTime;
    private long blackTime;

    private Color winner;

    private long intervallStart;

    private final int SpringerOffset[][]={{1,2},{-1,2},{1,-2},{-1,-2},{2,1},{-2,1},{2,-1},{-2,-1}};
    private final int KönigOffset[][]={{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};

    public ChessBoard(double timeInMin,int[][][] Board){
        this.Board=constructBoard(Board);
        currentPlayer=Color.WHITE;
        whiteTime=(long)timeInMin*60*1000;
        blackTime=(long)timeInMin*60*1000;

        //todo change to non fixed
        amountOfpieces=32;
        amountOfBlackpieces=16;
        amountOfWhitepieces=16;

        zuege=new ArrayList<ChessOperation>();
        zugId=0;
        intervallStart=System.currentTimeMillis();
        winner=null;
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

    public int getZugId(){
        return zugId;
    }

    public int[][] translateBoard(){

        int[][] chessBoard=new int[8][8];

        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                if(getPieceOn(i,j,Board)!=null){
                    chessBoard[i][j]=getPieceOn(i,j,Board).getIdFromType();
                }
            }
        }


        return chessBoard;
    }

    public int[][] translateColorBoard(){

        int[][] chessBoard=new int[8][8];

        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                if(getPieceOn(i,j,Board)!=null){
                    chessBoard[i][j]=getPieceOn(i,j,Board).getColor().getId();
                }
            }
        }


        return chessBoard;
    }

    public int[][] getEventBoard(Color color){
        int[][] chessBoard=new int[8][8];

        if(isKingUnderAttack(color)){
            int[] kingPos=getKingPos(color);
            chessBoard[kingPos[0]][kingPos[1]]=1;
        }

        return chessBoard;
    }

    public int getCurrentAmountOfPieces(Color color){
        if(color==Color.WHITE){
            return amountOfWhitepieces;
        }

        return amountOfBlackpieces;
    }

    public boolean nextStep(int from,int to){
        return nextStep((from-(from%10))/10,from%10,(to-(to%10))/10,to%10);
    }


    public boolean nextStep(int x, int y, int gotoX, int gotoY){
        if(winner!=null){
            return false;
        }

        if(!movePiece(x, y, gotoX, gotoY)){
            return false;
        }

        if(currentPlayer==Color.WHITE){
            whiteTime-=System.currentTimeMillis()-intervallStart;
        }else{
            blackTime-=System.currentTimeMillis()-intervallStart;
        }

        if(whiteTime<0){
            endGameFlag(Color.BLACK);
            return false;
        }

        if(blackTime<0){
            endGameFlag(Color.WHITE);
        }

        intervallStart=System.currentTimeMillis();


        if(isKingSurronded(currentPlayer)){
            endGameFlag(currentPlayer);
        }

        toggleCurrentPlayer();  
        zugId++;

        return true;

    }

    private boolean isCurrentKingUnderAttack(){
        return isKingUnderAttack(currentPlayer);
    }

    public boolean isKingSurronded(Color color){
        if(!isKingUnderAttack(color))
        return false;

        int[] kingPos=getKingPos(color);

        List<int[]> validCoordsOfKing = ValidCoordsOf(kingPos[0],kingPos[1],Board);

        if(validCoordsOfKing.size()==0)
        return true;
        
        return false;
    }

    private ChessPiece[][] getBoardWOKing(){
        ChessPiece[][] newBoard=new ChessPiece[8][8];

        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                ChessPiece currentPiece=getPieceOn(i, j, Board);

                if(currentPiece==null)
                continue;

                if(currentPiece.getType()==ChessPieceType.KOENIG)
                continue;

                newBoard[i][j]=currentPiece;
            }
        }

        return newBoard;
    }

    public int[] getKingPos(Color color){
        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                ChessPiece currentPiece=getPieceOn(i,j,Board);

                if(currentPiece==null)
                continue;

                if(currentPiece.getType()==ChessPieceType.KOENIG&&currentPiece.getColor()==color){
                    return new int[]{i,j};
                }
            }
        }

        return new int[0];
    }

    public boolean isKingUnderAttack(Color kingColor){
        
        int[] kingPos=getKingPos(kingColor);

        //System.out.println("foof"+kingPos[0]+kingPos[1]);

        return isPositionUnderAttack(kingPos[0], kingPos[1], kingColor,Board);
    }

    public boolean isPositionUnderAttack(int x,int y,Color currentColor,ChessPiece[][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(isPieceOn(i, j, currentColor,board)){
                    if(CoordInList(x,y,ValidCoordsOf(i, j,board))){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private Color colorInvert(Color color){
        if(color==Color.BLACK){
            return Color.WHITE;
        }else{
            return Color.BLACK;
        }
    }

    private void endGameFlag(Color winner){
        this.winner=winner;
    }

    public Color getWinner(){
        return winner;
    }

    public void toggleCurrentPlayer(){
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
        

        ChessPiece currentPiece=getPieceOn(x, y,Board);
        deletePieceOn(x, y);
        System.out.println(currentPiece.toString());
        ChessPiece previousPiece=putPieceOn(currentPiece, gotoX, gotoY);

        zuege.add(new ChessOperation(x, y, gotoX, gotoY, currentPiece, previousPiece));

        return true;
    }

    private Boolean moveIsValid(int x, int y, int gotoX, int gotoY){
        if(getPieceOn(x, y,Board).getColor()!=currentPlayer)
        return false;

        List<int[]> validCoords=ValidCoordsOf(x,y,Board);

        if(!CoordInList(gotoX, gotoY,validCoords))
        return false;  

        return true;
    }

    private boolean CoordInList(int x, int y,List<int[]> validCoords){
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
        if(!isPieceOn(x, y))
        return null;

        List<int[]> validCoords=ValidCoordsOf(x, y,Board);
        int[][] result=new int[8][8];

        for (int i = 0; i < validCoords.size(); i++) {
            result[validCoords.get(i)[0]][validCoords.get(i)[1]]=1;
        }
        
        return result;
    }

    public int[][] getHighlightOfColor(int x, int y,Color color){
        int[][] result=new int[8][8];

        if(!isPieceOn(x, y, color,Board)){
            return result;
        }

        List<int[]> validCoords=ValidCoordsOf(x, y,Board);

        for (int i = 0; i < validCoords.size(); i++) {
            result[validCoords.get(i)[0]][validCoords.get(i)[1]]=1;
        }
        
        return result;
    }

    public int[][] checkedGetHighlightOf(int x, int y){
        int[][] result=new int[8][8];

        if(!isPieceOn(x, y, currentPlayer,Board)){
            return result;
        }

        List<int[]> validCoords=ValidCoordsOf(x, y,Board);

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

    public long[] getTime(){
        return new long[]{whiteTime,blackTime};
    }

    public double[] getTimeInMin(){
        double[] result=new double[2];

        result[0]=((double)whiteTime)/1000/60;
        result[1]=((double)blackTime)/1000/60;

        return result;
    }   

    private List<int[]> ValidCoordsOf(int x,int y,ChessPiece[][] board){
        List<int[]>validCoords=new ArrayList<>();
        
        //System.out.println(getPieceOn(x, y).getType().name());

        switch (getPieceOn(x, y,board).getType()) {
            case BAUER:
                //Custom
                validCoords.addAll(getValidBauerCoords(x, y,board));
                break;
                        
            case TURM:
                //Horizontal
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, 0,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, 0,board));

                //Vertical
                validCoords.addAll(getValidIterrativeCoords(x, y, 0, 1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 0, -1,board));

                System.out.println(twoDArrtoString(validCoords));
                break;

            case SPRINGER:
                //Custom
                validCoords.addAll(getValidOffsetCoords(x, y, SpringerOffset,board));
                break;

            case LAUFER_W:
                //Diagonal
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, -1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, -1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, 1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, 1,board));

                break;

            case KOENIGIN:
                //Diagonal
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, -1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, -1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, 1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, 1,board));

                //Horizontal
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, 0,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, 0,board));

                //Vertical
                validCoords.addAll(getValidIterrativeCoords(x, y, 0, 1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 0, -1,board));
                break;

            case KOENIG:
                //Custom
                validCoords.addAll(getValidOffsetCoords(x, y, KönigOffset,board));   
                List<int[]>validUnattackedCoords=new ArrayList<>();
                
                for (int i = 0; i < validCoords.size(); i++) {
                    int[] currentPos=validCoords.get(i);

                    if(!isPositionUnderAttack(x, y, currentPlayer,getBoardWOKing())){
                        validUnattackedCoords.add(currentPos);
                    }
                }

                validCoords=validUnattackedCoords;
                break;

            case LAUFER_S:
                //Diagonal
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, -1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, -1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, 1, 1,board));
                validCoords.addAll(getValidIterrativeCoords(x, y, -1, 1,board));   
                break;
        }

        return validCoords;
    }

    private List<int[]> getValidOffsetCoords(int x,int y,int[][] offset,ChessPiece[][] board){
        List<int[]> validCoords=new ArrayList<>();

        int currentx;
        int currenty;

        for (int i = 0; i < offset.length; i++) {
            currentx=x+offset[i][0];
            currenty=y+offset[i][1];

            if((currentx>=8||currentx<=-1)||(currenty>=8||currenty<=-1))
                continue;

            if(!isPieceOn(currentx, currenty, currentPlayer,board))
                validCoords.add(new int[]{currentx,currenty});

        }

        return validCoords;
    }

    private List<int[]> getValidBauerCoords(int x, int y,ChessPiece[][] board){

        List<int[]> validCoords=new ArrayList<>();
        ChessPiece piece=getPieceOn(x, y,board);

        switch (piece.getColor()) {
            case WHITE:
                if(!isPieceOn(x+1,y,Color.WHITE,board)){
                    validCoords.add(new int[]{x+1,y});
                }

                if(!piece.gethasMoved()&&isInBounds(x, y)){
                    validCoords.add(new int[]{x+2,y});
                }

                if(isPieceOn(x+1, y+1, Color.BLACK,board)){
                    validCoords.add(new int[]{x+1,y+1});
                }

                if(isPieceOn(x+1, y-1, Color.BLACK,board)){
                    validCoords.add(new int[]{x+1,y-1});
                }
            break;

            case BLACK:

                if(!isPieceOn(x-1,y,Color.BLACK,board)){
                    validCoords.add(new int[]{x-1,y});
                }

                if(!piece.gethasMoved()&&isInBounds(x, y)){
                    validCoords.add(new int[]{x-2,y});
                }

                if(isPieceOn(x-1, y+1, Color.WHITE,board)){
                    validCoords.add(new int[]{x-1,y+1});
                }

                if(isPieceOn(x-1, y-1, Color.WHITE,board)){
                    validCoords.add(new int[]{x-1,y-1});
                }
            break;
        }
        return validCoords;
    }

    private List<int[]> getValidIterrativeCoords(int x,int y,int offsetX, int offsetY,ChessPiece[][] board){
        List<int[]> validCoords=new ArrayList<>();

        x+=offsetX;
        y+=offsetY;

        while (isInBounds(x, y)) {

            if(getPieceOn(x, y, board)==null){
                validCoords.add(new int[]{x,y});
                
                x+=offsetX;
                y+=offsetY;

                continue;
            }

            if(getPieceOn(x, y, board).getColor()!=currentPlayer){
                validCoords.add(new int[]{x,y});
                return validCoords;
            }else{
                return validCoords;
            }
        }
        return validCoords;
    }

    public ChessPiece getPieceOn(int x,int y,ChessPiece[][] board){
        if(!isInBounds(x, y))
        return null;

        return board[x][y];
    }

    private Boolean isPieceOn(int x,int y,Color color,ChessPiece[][] board){

        if(!isInBounds(x, y))
        return false;

        if(getPieceOn(x, y,board)==null)
        return false;

        return getPieceOn(x, y,board).getColor()==color;
    }

    private Boolean isPieceOn(int x,int y){
        return Board[x][y]!=null;
    }

    private void deletePieceOn(int x,int y){
        Board[x][y]=null;
    }

    private ChessPiece putPieceOn(ChessPiece piece,int x,int y){
        ChessPiece previousPiece=getPieceOn(x, y,Board);

        if(previousPiece!=null){
            amountOfpieces--;
            if(previousPiece.getColor()==Color.WHITE){
                amountOfWhitepieces--;
            }else{
                amountOfBlackpieces--;
            }
        }

        piece.sethasMovedTrue();

        Board[x][y]=piece;

        return previousPiece;  
    }

    public String pieceToString(int x, int y){
        return getPieceOn(x, y,Board).toString();
    }

    @Override
    public String toString(){

        String result="";

        for (int i = 0; i < Board.length; i++) {
            result+=Integer.toString(i+1)+"  ";
            for (int j = 0; j < Board[i].length; j++) {
                if(getPieceOn(i, j,Board)!=null){
                    result+="  ";
                    switch (getPieceOn(i, j,Board).getType()) {
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

    public String twoDArrtoString(List<int[]> targetList){
        String result="";

        int[][] target= new int[8][8];

        for (int i = 0; i < targetList.size(); i++) {
            target[targetList.get(i)[0]][targetList.get(i)[1]]=1;
            System.out.println("foof");
        }


        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                result+=" "+target[i][j]+" ";
            }
            result+="\n";
        }

        return result;
    }
}
