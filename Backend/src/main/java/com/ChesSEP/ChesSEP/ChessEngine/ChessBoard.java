package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.List;



public class ChessBoard {

    public ChessPiece[][] chessBoard;

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
        chessBoard=constructBoard(Board);
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

    //ImportBoard

    public ChessPiece[][] constructBoard(int[][][] boardToConstruct){
        ChessPiece[][] constructedBoard = new ChessPiece[8][8];

        for (int i = 0; i < boardToConstruct.length; i++) {
            for (int j = 0; j < boardToConstruct[i].length; j++) {
                if(boardToConstruct[i][j][0]!=0)
                    constructedBoard[i][j] = new ChessPiece(boardToConstruct[i][j][0], boardToConstruct[i][j][1]);
            }
        }

        return constructedBoard;
    }

    public int getZugId(){
        return zugId;
    }


    //ExportBoard

    public int[][] translateBoard(){
        int[][] resultBoard=new int[8][8];

        for (int i = 0; i < resultBoard.length; i++) {
            for (int j = 0; j < resultBoard[i].length; j++) {
                if(chessBoard[i][j]==null)
                    continue;

                resultBoard[i][j]=chessBoard[i][j].getIdFromType();
            }
        }
        return resultBoard;
    }

    public int[][] translateColorBoard(){
        int[][] resultBoard=new int[8][8];

        for (int i = 0; i < resultBoard.length; i++) {
            for (int j = 0; j < resultBoard[i].length; j++) {
                if(chessBoard[i][j]==null)
                    continue;

                resultBoard[i][j]=chessBoard[i][j].getColor().getId();
            }
        }
        return resultBoard;
    }

    public int[][] getEventBoard(Color color){
        int[][] resultBoard=new int[8][8];

        if(!isKingUnderAttack(color))
            return resultBoard;

        int[] kingsCoords=getKingPos(color);

        resultBoard[kingsCoords[0]][kingsCoords[1]]=1;

        return resultBoard;
    }

    public int getCurrentAmountOfPieces(Color color){
        if(color==Color.BLACK){
            return amountOfBlackpieces;
        }else{
            return amountOfWhitepieces;
        }
    }

    public Color getCurrentActivePlyer(){
        return currentPlayer;
    }

    public long getTimeLong(Color color){
        if(color==Color.BLACK){
            return blackTime;
        }else{
            return whiteTime;
        }
    }

    private void setTimeLong(Color color,long value){
        if(color==Color.BLACK){
            blackTime=value;
        }else{
            whiteTime=value;
        }
    }

    public int[] getTime(Color color){
        double currentTime;
        if(color==Color.BLACK){
            currentTime=(double)blackTime;
        }else{
            currentTime=(double)whiteTime;
        }

        currentTime=currentTime/1000/60;

        double seconds=(currentTime%1)*60;                       
        double minutes=currentTime-(currentTime%1);

        return new int[]{(int)minutes,(int)seconds};
    }   


    //EndCondition

    private int[] getKingPos(Color kingsColor) {

        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[i].length; j++) {
                ChessPiece currentPiece=getPieceOn(i, j, chessBoard);

                if(currentPiece==null)
                    continue;

                if(currentPiece.getColor()==kingsColor&&currentPiece.getType()==ChessPieceType.KOENIG)
                    return new int[]{i,j};
            }
        }

        return null;
    }

    private boolean isKingUnderAttack(Color kingsColor) {
        int[] kingPos=getKingPos(kingsColor);

        if(kingPos==null)
            return false;

        return isPositionUnderAttack(kingPos[0], kingPos[1], kingsColor, chessBoard);
    }

    //TODO check for your MOM
    private boolean isKingCheckmate(Color kingsColor){
        if(!isKingUnderAttack(kingsColor))
            return false;

        int[] kingPos=getKingPos(kingsColor);
        
        if(validCoordsOf(kingPos[0], kingPos[1], chessBoard).size()!=0)
            return false;

        

        return true;
    }

    private boolean isPositionUnderAttack(int x,int y,Color alliedColor,ChessPiece[][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                ChessPiece currentPiece=getPieceOn(i, j, board);

                if(currentPiece==null)
                    continue;

                if(currentPiece.getColor()==alliedColor||currentPiece.getType()==ChessPieceType.KOENIG)
                    continue;

                List<int[]> currentValidEnemyCoords=validCoordsOf(i, j, board);
                
                if(doesListContainCoords(x, y, currentValidEnemyCoords))
                    return true;
            }
        }

        return false;
    }

    private void endGameFlag(Color loser){
        if(loser==Color.BLACK){
            winner=Color.WHITE;
        }else{
            winner=Color.BLACK;
        }
    }

     public Color getWinner() {
        return winner;
    }

    //BoardManagement

    private ChessPiece getPieceOn(int x, int y, ChessPiece[][] board) {
        if(!isInBounds(x, y))
            return null;
        
        return board[x][y];
    }

    private boolean isPieceOn(int x,int y, ChessPiece[][] board){
        ChessPiece currentPiece=getPieceOn(x, y, board);

        if(currentPiece==null)
            return false;

        return true;
    }

    private boolean isPieceOn(int x,int y,Color colorOfThePiece, ChessPiece[][] board){
        ChessPiece currentPiece=getPieceOn(x, y, board);

        if(currentPiece==null)
            return false;

        return currentPiece.getColor()==colorOfThePiece;
    }

    private ChessPiece[][] getBoardWOKing(Color kingToRemove){

        int[] kingToRemoveCoords=getKingPos(kingToRemove);

        ChessPiece[][] resultBoard=new ChessPiece[8][8];

        for (int i = 0; i < resultBoard.length; i++) {
            for (int j = 0; j < resultBoard[i].length; j++) {

                if(i==kingToRemoveCoords[0]&&j==kingToRemoveCoords[1])
                    continue;
                
                resultBoard[i][j]=chessBoard[i][j];
            }
        }

        return resultBoard;
    }

    private boolean doesListContainCoords(int x, int y, List<int[]> validCoordsOfcurrentPiece) {

        for (int i = 0; i < validCoordsOfcurrentPiece.size(); i++) {

            int currentX=validCoordsOfcurrentPiece.get(i)[0];
            int currentY=validCoordsOfcurrentPiece.get(i)[1];

            if(currentX==x&&currentY==y)
            return true;
        }

        return false;
    }

    private boolean isInBounds(int x, int y){
        if((x>=8||x<=-1)||(y>=8||y<=-1)){
            return false;
        }

        return true;
    }

    private Color getEnemyColorOf(ChessPiece alliedPiece){
        if(alliedPiece.getColor()==Color.BLACK){
            return Color.WHITE;
        }else{
            return Color.BLACK;
        }
    }


    //MakeAMove

    public boolean nextStep(int from,int to){
        return nextStep((from-(from%10))/10,from%10,(to-(to%10))/10,to%10);
    }

    public boolean nextStep(int x, int y, int gotoX, int gotoY){

        if(!moveIsValid(x, y, gotoX, gotoY))
            return false;

        movePiece(x, y, gotoX, gotoY);

        if(isKingUnderAttack(currentPlayer))
            endGameFlag(currentPlayer);

        timeManager();

        toggleCurrentPlayer();

        return true;
    }

    private void timeManager(){
        long currentTime=getTimeLong(currentPlayer);

        currentTime-=System.currentTimeMillis()-intervallStart;
        intervallStart=System.currentTimeMillis();

        setTimeLong(currentPlayer, currentTime);
    }

    private void toggleCurrentPlayer(){
        if(currentPlayer==Color.WHITE){
            currentPlayer=Color.BLACK;
        }else{
            currentPlayer=Color.WHITE;
        }
    }

    private boolean moveIsValid(int x, int y, int gotoX, int gotoY){

        if(!isInBounds(x, y)||!isInBounds(gotoX, gotoY))
        return false;

        ChessPiece currentChessPiece=getPieceOn(x, y, chessBoard);

        if(currentChessPiece==null)
        return false;

        List<int[]> validCoordsOfcurrentPiece=validCoordsOf(x, y, chessBoard);

        return doesListContainCoords(gotoX, gotoY, validCoordsOfcurrentPiece);
    }


    private void movePiece(int x, int y, int gotoX, int gotoY){
        
        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);
        ChessPiece preveiousPiece=getPieceOn(gotoX, gotoY, chessBoard);

        currentPiece.sethasMovedTrue();

        chessBoard[x][y]=null;
        chessBoard[gotoX][gotoY]=currentPiece;

        ChessOperation currentOperation=new ChessOperation(x, y, gotoX, gotoY, currentPiece, preveiousPiece);
        zuege.add(currentOperation);
    }

    //ValidMoves

    private List<int[]> validCoordsOf(int x,int y,ChessPiece[][] board){

        List<int[]> resultValidCoords=new ArrayList<>();

        ChessPiece currentChessPiece=getPieceOn(x, y, board);

        if(currentChessPiece==null)
        return resultValidCoords;

        switch (currentChessPiece.getType()) {
            case TURM:
                //Horizontal/Vertical
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,-1,0,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,+1,0,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,0,-1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,0,+1,board));
            break;

            case SPRINGER:
                resultValidCoords.addAll(getValidOffsetCoords(x, y, SpringerOffset, board));
            break;

            case LAUFER:
                //Diagonal
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,+1,+1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,+1,-1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,-1,+1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,-1,-1,board));
            break;

            case KOENIGIN:
                //Horizontal/Vertical
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,-1,0,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,+1,0,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,0,-1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,0,+1,board));

                //Diagonal
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,+1,+1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,+1,-1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,-1,+1,board));
                resultValidCoords.addAll(getValidIterrativeCoords(x,y,-1,-1,board));
            break;

            case KOENIG:
                resultValidCoords.addAll(getValidOffsetCoords(x, y, KönigOffset, board));
                List<int[]>validUnattackedCoords=new ArrayList<>();

                ChessPiece[][] boardWOcurrentKing=getBoardWOKing(currentChessPiece.getColor());

                for (int i = 0; i < resultValidCoords.size(); i++) {
                    int currentX=resultValidCoords.get(i)[0];
                    int currentY=resultValidCoords.get(i)[1];

                    if(!isPositionUnderAttack(currentX, currentY, currentChessPiece.getColor(), boardWOcurrentKing))
                      validUnattackedCoords.add(new int[]{currentX,currentY});  
                }
                resultValidCoords=validUnattackedCoords;
            break;

            case BAUER:
                resultValidCoords.addAll(getValidBauerCoords(x, y, board));
            break;
        }

        return resultValidCoords;
    }

    private List<int[]> getValidOffsetCoords(int x,int y,int[][] offset,ChessPiece[][] board){

        List<int[]> validCoords=new ArrayList<>();

        ChessPiece currentPiece=getPieceOn(x, y, board);

        if(currentPiece==null)
            return validCoords;

        int currentx;
        int currenty;

        for (int i = 0; i < offset.length; i++) {
            currentx=x+offset[i][0];
            currenty=y+offset[i][1];

            if(!isInBounds(currentx, currenty))
                continue;

            if(!isPieceOn(currentx, currenty, currentPlayer,board))
                validCoords.add(new int[]{currentx,currenty});

        }

        return validCoords;
    }

    private List<int[]> getValidBauerCoords(int x, int y,ChessPiece[][] board){

        List<int[]> resultValidCoords=new ArrayList<>();

        ChessPiece currentPiece=getPieceOn(x, y, board);
        int movingDirection;

        if(currentPiece==null)
        return null;

        if(currentPiece.getColor()==Color.BLACK){
            movingDirection=-1;
        }else{
            movingDirection=1;
        }

        if(!isPieceOn(x+movingDirection, y, board)||isPieceOn(x+movingDirection, y,getEnemyColorOf(currentPiece), board))
            resultValidCoords.add(new int[]{x+movingDirection,y});
        

        if(currentPiece.gethasMoved()==false){
            if(!isPieceOn(x+movingDirection*2, y, board)||isPieceOn(x+movingDirection*2, y,getEnemyColorOf(currentPiece), board)){
                resultValidCoords.add(new int[]{x+movingDirection*2,y});
            }
        }

        if(isPieceOn(x+movingDirection, y+1,getEnemyColorOf(currentPiece), board))
            resultValidCoords.add(new int[]{x+movingDirection,y+1});

        if(isPieceOn(x+movingDirection, y-1,getEnemyColorOf(currentPiece), board))
        resultValidCoords.add(new int[]{x+movingDirection,y-1});

        return resultValidCoords;
    }

    private List<int[]> getValidIterrativeCoords(int x,int y,int offsetX, int offsetY,ChessPiece[][] board){

        List<int[]> resultValidCoords=new ArrayList<>();

        ChessPiece currentPiece=getPieceOn(x, y, board);

        x=x+offsetX;
        y=y+offsetY;

        while (isInBounds(x, y)) {
            
            if(!isPieceOn(x, y, board)){
                resultValidCoords.add(new int[]{x,y});

                x=x+offsetX;
                y=y+offsetY;

                continue;
            }

            if(isPieceOn(x, y, currentPiece.getColor(), board))
                return resultValidCoords;

            if(isPieceOn(x, y, getEnemyColorOf(currentPiece), board)){
                resultValidCoords.add(new int[]{x,y});
                return resultValidCoords;
            }

        }

        return resultValidCoords;
    }

    public int[][] GetHighlightOf(int x, int y){

        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);

        if(currentPiece==null)
            return null;

        List<int[]> validCoords=validCoordsOf(x, y, chessBoard);

        return fillCoordsIntoArray(validCoords);
    }

    public int[][] checkedGetHighlightOf(int x, int y){

        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);

        if(currentPiece==null||currentPiece.getColor()!=currentPlayer)
            return null;
        
        List<int[]> validCoords=validCoordsOf(x, y, chessBoard);

        return fillCoordsIntoArray(validCoords);
    }

    private int[][] fillCoordsIntoArray(List<int[]> coordsList){
        int[][] coordArr=new int[8][8];

        for (int i = 0; i < coordsList.size(); i++) {
            coordArr[coordsList.get(i)[0]][coordsList.get(i)[1]]=1;
        }

        return coordArr;
    }


    @Override
    public String toString(){

        String result="";

        for (int i = 0; i < chessBoard.length; i++) {
            result+=Integer.toString(i+1)+"  ";
            for (int j = 0; j < chessBoard[i].length; j++) {
                if(getPieceOn(i, j,chessBoard)!=null){
                    result+="  ";
                    switch (getPieceOn(i, j,chessBoard).getType()) {
                        case BAUER:
                            result+="  "+ChessPieceType.BAUER.name()+" ";
                            break;
                        
                        case TURM:
                            result+="  "+ChessPieceType.TURM.name()+"  ";
                            break;

                        case SPRINGER:
                            result+=ChessPieceType.SPRINGER.name();
                            break;

                        case LAUFER:
                            result+=" "+ChessPieceType.LAUFER.name()+" ";
                            break;

                        case KOENIGIN:
                            result+=ChessPieceType.KOENIGIN.name();
                            break;

                        case KOENIG:
                            result+=" "+ChessPieceType.KOENIG.name()+" ";
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
