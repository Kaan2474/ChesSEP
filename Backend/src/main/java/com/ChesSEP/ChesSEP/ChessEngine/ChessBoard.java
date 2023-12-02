package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.List;



public class ChessBoard {

    public ChessPiece[][] chessBoard;

    private List<ChessOperation> zuege;

    private Color currentPlayer;

    private long whiteTime;
    private long blackTime;

    private Color winner;

    private long intervallStart;

    private boolean transformBauer;

    private final int SpringerOffset[][]={{1,2},{-1,2},{1,-2},{-1,-2},{2,1},{-2,1},{2,-1},{-2,-1}};
    private final int KönigOffset[][]={{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};

    public ChessBoard(double timeInMin,int[][][] Board){
        chessBoard=constructBoard(Board);
        currentPlayer=Color.WHITE;
        whiteTime=(long)timeInMin*60*1000;
        blackTime=(long)timeInMin*60*1000;

        zuege=new ArrayList<ChessOperation>();
        intervallStart=System.currentTimeMillis();

        transformBauer=false;
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
        return zuege.size();
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

    public int[][] getKingBoard(Color color){
        int[][] resultBoard=new int[8][8];

        if(isKingUnderAttack(color,chessBoard)==null)
            return resultBoard;

        int[] kingsCoords=getKingPos(color);

        resultBoard[kingsCoords[0]][kingsCoords[1]]=1;

        return resultBoard;
    }

    public int[][] getLastMove(){
        int[][] resultBoard=new int[8][8];

        if(zuege.size()==0)
            return resultBoard;

        ChessOperation lastMove=zuege.get(zuege.size()-1);

        resultBoard[lastMove.x][lastMove.y]=1;

        resultBoard[lastMove.newX][lastMove.newY]=2;

        return resultBoard;
    }

    public int[][] getBauerTransformEvent(){
        int[][] resultBoard=new int[8][8];

        int[] bauerCoords=getBauerToTransform();

        if(bauerCoords==null)
            return resultBoard;

        resultBoard[bauerCoords[0]][bauerCoords[1]]=1;

        return resultBoard;
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

    public boolean hasBauerToTransform(){
        return transformBauer;
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

    private int[] isKingUnderAttack(Color kingsColor,ChessPiece[][] board) {
        int[] kingPos=getKingPos(kingsColor);

        if(kingPos==null)
            return null;

        return isPositionUnderAttack(kingPos[0], kingPos[1], kingsColor, board);
    }

    private boolean isKingCheckmate(Color kingsColor){

        int[] attackerCoords=isKingUnderAttack(kingsColor,chessBoard);

        if(attackerCoords==null)
            return false;

        int[] kingPos=getKingPos(kingsColor);
        
        if(validCoordsOf(kingPos[0], kingPos[1], chessBoard).size()!=0)
            return false;

        if(isTheAttackBlockable(kingsColor,attackerCoords[0],attackerCoords[1],chessBoard))
            return false;

        return true;
    }

    /**
     * Must check if king is under attack and if he could move before executing if he can dont do this !!!
     * @param kingsColor
     * @param attackerX
     * @param attackerY
     * @param board
     * @return boolean if the king could be saved
     */
    private boolean isTheAttackBlockable(Color kingsColor, int attackerX, int attackerY,ChessPiece[][] board){

        int[] kingsCoords=getKingPos(kingsColor);

        ChessPiece currentKing=getPieceOn(kingsCoords[0], kingsCoords[1], board);

        List<int[]> validAttackerCoords=validCoordsOf(attackerX, attackerY, board);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(!isPieceOn(i, j, currentKing.getColor(), board))
                    continue;
                
                ChessPiece currentPiece=getPieceOn(i, j, board);

                if(currentPiece.getType()==ChessPieceType.KOENIG)
                    continue;

                List<int[]> currentValidCoords=validCoordsOf(i, j, board);

                for (int k = 0; k < validAttackerCoords.size(); k++) {
                    for (int l = 0; l < currentValidCoords.size(); l++) {
                        int[] currentAlliedCoords=currentValidCoords.get(l);

                        if(!doesListContainCoords(currentAlliedCoords[0], currentAlliedCoords[1], validAttackerCoords))
                            continue;

                        ChessPiece[][] testBoard=createNextBoard(i, j, currentAlliedCoords[0], currentAlliedCoords[1]);

                        if(isKingUnderAttack(kingsColor, testBoard)==null)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private ChessPiece[][] createNextBoard(int x, int y, int gotoX, int gotoY){

        ChessPiece[][] nextBoard=copyBoard(chessBoard);

        testMovePieceOnBoard(x, y, gotoX, gotoY, nextBoard);

        return nextBoard;
    }

    private int[] isPositionUnderAttack(int x,int y,Color alliedColor,ChessPiece[][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                ChessPiece currentPiece=getPieceOn(i, j, board);

                if(currentPiece==null)
                    continue;

                if(currentPiece.getColor()==alliedColor||currentPiece.getType()==ChessPieceType.KOENIG)
                    continue;

                List<int[]> currentValidEnemyCoords=validCoordsOf(i, j, board);
                
                if(doesListContainCoords(x, y, currentValidEnemyCoords))
                    return new int[]{i,j};
            }
        }

        return null;
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

        ChessPiece[][] resultBoard=copyBoard(chessBoard);

        resultBoard[kingToRemoveCoords[0]][kingToRemoveCoords[1]]=null;

        return resultBoard;
    }

    private ChessPiece[][] copyBoard(ChessPiece[][] boardToCopy){
        ChessPiece[][] copiedBoard=new ChessPiece[8][8];

        for (int i = 0; i < copiedBoard.length; i++) {
            for (int j = 0; j < copiedBoard[i].length; j++) {
                copiedBoard[i][j]=boardToCopy[i][j];
            }
        }

        return copiedBoard;
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

        if(winner!=null)
            return false;

        if(transformBauer)
            return false;

        if(!moveIsValid(x, y, gotoX, gotoY))
            return false;

        movePiece(x, y, gotoX, gotoY);

        //kign can be slain without resistance next Turn SOLLTE NICHT EINTREFFEN
        if(isKingUnderAttack(currentPlayer,chessBoard)!=null)
            endGameFlag(currentPlayer);


        if(getBauerToTransform()!=null){
            transformBauer=true;
        }else{
            timeManager();
            toggleCurrentPlayer();
        }

        //usavable Situation
        if(isKingCheckmate(currentPlayer))
            endGameFlag(currentPlayer);

        return true;
    }

    public boolean transformBauer(int id){
        if(!transformBauer||id>5||id<2)
            return false;

        int[] currentBauerCoords=getBauerToTransform();

        ChessPiece currentPiece=getPieceOn(currentBauerCoords[0], currentBauerCoords[1], chessBoard);

        if(currentPiece==null)
            return false;

        chessBoard[currentBauerCoords[0]][currentBauerCoords[1]]=new ChessPiece(id, currentPiece.getColor().getId());

        timeManager();
        toggleCurrentPlayer();

        transformBauer=false;
        return true;
    }

    private int[] getBauerToTransform(){

        for (int i = 0; i < chessBoard[0].length; i++) {
            ChessPiece currentPiece=getPieceOn(0, i, chessBoard);

            if(currentPiece==null||currentPiece.getColor()==Color.WHITE||currentPiece.getType()!=ChessPieceType.BAUER)
                continue;

            return new int[]{0,i};
        }

        for (int i = 0; i < chessBoard[7].length; i++) {
            ChessPiece currentPiece=getPieceOn(7, i, chessBoard);

            if(currentPiece==null||currentPiece.getColor()==Color.BLACK||currentPiece.getType()!=ChessPieceType.BAUER)
                continue;

            return new int[]{7,i};
        }

        return null;
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

        validCoordsOfcurrentPiece=testMovesForCheckMate(x, y, validCoordsOfcurrentPiece);

        return doesListContainCoords(gotoX, gotoY, validCoordsOfcurrentPiece);
    }


    private void movePiece(int x, int y, int gotoX, int gotoY){

        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);

        ChessPiece preveiousPiece=getPieceOn(gotoX, gotoY, chessBoard);
        
        if(moveIsEnPassant(y,gotoY)){
            int moveDirection;

            if(currentPiece.getColor()==Color.BLACK){
                moveDirection=-1;
            }else{
                moveDirection=+1;
            }

            preveiousPiece=getPieceOn(gotoX, gotoY+moveDirection, chessBoard);
            chessBoard[x][y+moveDirection]=null;
        }

        chessBoard[x][y]=null;
        chessBoard[gotoX][gotoY]=currentPiece;

        ChessOperation currentOperation=new ChessOperation(x, y, gotoX, gotoY, currentPiece, preveiousPiece);
        zuege.add(currentOperation);

        currentPiece.sethasMovedTrue(zuege.size());
    }

    private boolean moveIsEnPassant(int y,int gotoY){
        return y!=gotoY;
    }

    private void testMovePieceOnBoard(int x, int y, int gotoX, int gotoY,ChessPiece[][]board){
        ChessPiece currentPiece=getPieceOn(x, y, board);

        board[x][y]=null;
        board[gotoX][gotoY]=currentPiece;
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

                    if(isPositionUnderAttack(currentX, currentY, currentChessPiece.getColor(), boardWOcurrentKing)==null)
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

    private List<int[]> testMovesForCheckMate(int x,int y,List<int[]> potentiallyValidMoves){

        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);

        if(currentPiece==null)
            return null;
        
        List<int[]> validResultMoves=new ArrayList<>();

        ChessPiece[][] testBoard;

        for (int i = 0; i < potentiallyValidMoves.size(); i++) {
            int[] currentPos=potentiallyValidMoves.get(i);

            testBoard=copyBoard(chessBoard);

            testMovePieceOnBoard(x, y, currentPos[0], currentPos[1], testBoard);

            if(isKingUnderAttack(currentPiece.getColor(), testBoard)==null)
                validResultMoves.add(currentPos);
        }

        return validResultMoves;
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

        if(!isPieceOn(x+movingDirection, y, board)||isPieceOn(x+movingDirection, y,getEnemyColorOf(currentPiece), board)){

            resultValidCoords.add(new int[]{x+movingDirection,y});

            if(currentPiece.hasMoved()==false){

                if(isInBounds(x+movingDirection*2, y)){
                    if(!isPieceOn(x+movingDirection*2, y, board)||isPieceOn(x+movingDirection*2, y,getEnemyColorOf(currentPiece), board)){
                        resultValidCoords.add(new int[]{x+movingDirection*2,y});
                    }
                }   
            }
        }

        if(isPieceOn(x+movingDirection, y+1,getEnemyColorOf(currentPiece), board))
            resultValidCoords.add(new int[]{x+movingDirection,y+1});

        if(isPieceOn(x+movingDirection, y-1,getEnemyColorOf(currentPiece), board))
        resultValidCoords.add(new int[]{x+movingDirection,y-1});

        if(isPieceOn(x, y-1, getEnemyColorOf(currentPiece), board)){
            ChessPiece currentEnemyPiece=getPieceOn(x, y-1, board);

            if(currentEnemyPiece.getType()==ChessPieceType.BAUER&&currentEnemyPiece.whenDidThePieceMove()==getZugId())
                resultValidCoords.add(new int[]{x+movingDirection,y-1});
        }

        if(isPieceOn(x, y+1, getEnemyColorOf(currentPiece), board)){
            ChessPiece currentEnemyPiece=getPieceOn(x, y+1, board);

            if(currentEnemyPiece.getType()==ChessPieceType.BAUER&&currentEnemyPiece.whenDidThePieceMove()==getZugId())
                resultValidCoords.add(new int[]{x+movingDirection,y+1});
        }

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

        validCoords=testMovesForCheckMate(x, y, validCoords);

        return fillCoordsIntoArray(validCoords);
    }

    public int[][] checkedGetHighlightOf(int x, int y){

        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);

        if(currentPiece==null||currentPiece.getColor()!=currentPlayer)
            return null;
        
        List<int[]> validCoords=validCoordsOf(x, y, chessBoard);

        validCoords=testMovesForCheckMate(x, y, validCoords);

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

        String result="   "+"      A      "+"      B      "+"      C      "+"      D      "+"      E      "+"      F      "+"      G      "+"     H     \n";

        for (int i = 0; i < chessBoard.length; i++) {
            result+=Integer.toString(i+1)+"  ";
            for (int j = 0; j < chessBoard[i].length; j++) {

                ChessPiece currentPiece=getPieceOn(i, j, chessBoard);

                if(currentPiece!=null){
                    result+="  ";

                    switch (getPieceOn(i, j,chessBoard).getType()) {
                        case BAUER:
                            result+="  "+ChessPieceType.BAUER.name()+currentPiece.getColor().getId()+" ";
                            break;
                        
                        case TURM:
                            result+="  "+ChessPieceType.TURM.name()+currentPiece.getColor().getId()+"  ";
                            break;

                        case SPRINGER:
                            result+=ChessPieceType.SPRINGER.name()+currentPiece.getColor().getId();
                            break;

                        case LAUFER:
                            result+=" "+ChessPieceType.LAUFER.name()+currentPiece.getColor().getId()+" ";
                            break;

                        case KOENIGIN:
                            result+=ChessPieceType.KOENIGIN.name()+currentPiece.getColor().getId();
                            break;

                        case KOENIG:
                            result+=" "+ChessPieceType.KOENIG.name()+currentPiece.getColor().getId()+" ";
                            break;
                    }
                    result+="  ";
                }else{
                    result+="     [  ]    ";
                }
                
            }
            result+=Integer.toString(i+1)+"  ";
            result+="\n";
        }

        result+="    "+"      A      "+"      B      "+"      C      "+"      D      "+"      E      "+"      F      "+"      G      "+"     H     \n";
        return result;
    }
}
