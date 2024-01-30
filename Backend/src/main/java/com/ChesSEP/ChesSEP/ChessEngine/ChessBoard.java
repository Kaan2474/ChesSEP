package com.ChesSEP.ChesSEP.ChessEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoard {

    public ChessPiece[][] chessBoard;

    public List<ChessOperation> zuege;

    private Color currentPlayer;

    private long whiteTime;
    private long blackTime;

    private Color winner;
    private boolean isRemis;
    private int remisCounterWhite;
    private int remisCounterBlack;
    private boolean requestRemisWhite;
    private boolean requestRemisBlack;
    private Map<Integer,ChessPiece[][]> remisPattern;
    private Map<Integer,Integer[]> remisPatternStatus;

    public int[] AssistanceCounter={0,0};
    
    private long intervallStart;

    private boolean bauerTransform;

    private final int SpringerOffset[][]={{1,2},{-1,2},{1,-2},{-1,-2},{2,1},{-2,1},{2,-1},{-2,-1}};
    private final int KönigOffset[][]={{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};

    private ChessGameType gameType;
    private int[][] puzzleMoves;
    private Color puzzlePlayerColor;

    private int[] enPassantSquare;

    private ChessOperation letzterZug;

    private difficulty gameDifficulty;

    public ChessBoard(double timeInMin,int[][][] Board){
        chessBoard=constructBoard(Board);
        gameType=ChessGameType.PVP;
        currentPlayer=Color.WHITE;
        whiteTime=(long)timeInMin*60*1000;
        blackTime=(long)timeInMin*60*1000;

        zuege=new ArrayList<ChessOperation>();
        intervallStart=System.currentTimeMillis();

        bauerTransform=false;
        winner=null;
        isRemis=false;
        remisCounterWhite=0;
        remisCounterBlack=0;
        remisPattern=new HashMap<>();
        remisPatternStatus=new HashMap<>();
        enPassantSquare=new int[]{-1,-1,-1};
        letzterZug=null;
        
        remisPatternManager();
    }

    public ChessBoard(int[] status,int[][][] Board,int[][] moves){
        chessBoard=constructBoard(Board);
        setStatusToBoard(status, chessBoard);

        gameType=ChessGameType.PUZZLE;

        if(status[0]==1){
            currentPlayer=Color.WHITE;
            puzzlePlayerColor=Color.BLACK;
        }else{
            currentPlayer=Color.BLACK;
            puzzlePlayerColor=Color.WHITE;
        }

        zuege=new ArrayList<ChessOperation>();
        bauerTransform=false;

        winner=null;
        isRemis=false;
        remisCounterWhite=0;
        remisCounterBlack=0;
        remisPattern=new HashMap<>();
        remisPatternStatus=new HashMap<>();
        enPassantSquare=new int[]{-1,-1,-1};;

        puzzleMoves=moves;
        letzterZug=null;

        nextEnemyStep();
    }


    //ForTestingAndBots
    public ChessBoard(String FEN){
        chessBoard=constructBoardFromFEN(FEN);
        gameType=ChessGameType.PVP;
        currentPlayer=Color.WHITE;

        bauerTransform=false;
        winner=null;
       
        enPassantSquare=new int[]{-1,-1,-1};
        letzterZug=null;
    }

    //ForChessBot
    public ChessBoard(){
        gameType=ChessGameType.PVP;
        currentPlayer=Color.WHITE;

        bauerTransform=false;
        winner=null;
       
        enPassantSquare=new int[]{-1,-1,-1};
        zuege=new ArrayList<ChessOperation>();
        letzterZug=null;
    }

    //PVE
    public ChessBoard(int[][][] Board,int difficulty){
        chessBoard=constructBoard(Board);

        gameDifficulty=setDifficulty(difficulty);
        currentPlayer=Color.WHITE;

        gameType=ChessGameType.PVE;

        zuege=new ArrayList<ChessOperation>();
        bauerTransform=false;

        winner=null;
        isRemis=false;
        remisCounterWhite=0;
        remisCounterBlack=0;
        remisPattern=new HashMap<>();
        remisPatternStatus=new HashMap<>();
        enPassantSquare=new int[]{-1,-1,-1};;

        letzterZug=null;
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

    private difficulty setDifficulty(int id){
        switch (id) {
            case 0:
                return difficulty.EASY;
            case 1:
                return difficulty.MEDIUM;
            default:
                return difficulty.HARD;
        }
    }

    public ChessPiece[][] constructBoardFromFEN(String FEN){
        ChessPiece[][] constructedBoard = new ChessPiece[8][8];
        String[] FENarr=FEN.split("/|\s");

        for (int i = 0; i < 8; i++) {
            String currentLine=FENarr[i];
            int offset=0;
            for (int j = 0; j < currentLine.length(); j++) {
                String currentChar=""+currentLine.charAt(j);

                if(currentChar.matches("[1-8]")){
                    for (int k = 0; k <= Integer.parseInt(currentChar); k++) {
                        offset++;
                    }
                    continue;
                }

                int[] currentPieceIDs=translateToIdNotation(currentChar.charAt(0));
                
                constructedBoard[i][j+offset]=new ChessPiece(currentPieceIDs[0],currentPieceIDs[1]);
            }
        }

        setStatusToBoard(extractStatusFromFEN(FEN), constructedBoard);

        return constructedBoard;
    }

    private int[] extractStatusFromFEN(String FEN){
        String[] statusArr=FEN.split("\s");

        int[] castlingAvailability=parseCastlingAvailability(statusArr[2]);

        int[] resultArr=new int[]{
            translateColorToId(statusArr[1]),   //ActiveColor  
            castlingAvailability[0],    //Castling Availability Q
            castlingAvailability[1],    //K
            castlingAvailability[2],    //q
            castlingAvailability[3],    //k
            translateCoord(statusArr[3])};   //En Passnat TargetSquare

        return resultArr;
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

    

    private void setStatusToBoard(int[] status,ChessPiece[][] board){

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                ChessPiece currentPiece=getPieceOn(j, i, board);

                if(currentPiece==null)
                    continue;

                if(!isBauerInDefaultPosition(j, i, board))
                    currentPiece.setHasMoved(true);

            }
        }

        if(status[1]==1){
            board[7][0].setHasMoved(false);
            board[7][4].setHasMoved(false);
        }

        if(status[2]==1){
            board[7][7].setHasMoved(false);
            board[7][4].setHasMoved(false);
        }

        if(status[3]==1){
            board[0][0].setHasMoved(false);
            board[0][4].setHasMoved(false);
        }

        if(status[4]==1){
            board[0][7].setHasMoved(false);
            board[0][4].setHasMoved(false);
        }
    }

    private boolean isBauerInDefaultPosition(int x,int y,ChessPiece[][] board){
        
        ChessPiece currentPiece=getPieceOn(x, y, board);

        if(currentPiece==null)
            return false;

        if(currentPiece.getType()!=ChessPieceType.BAUER)
            return false;
        
        if(x==1||x==6)
            return true;
        
        return false;
    }

    public int getZugId(){
        return zuege.size();
    }


    //ExportBoard

    public ChessGameType getGameType(){
        return gameType;
    }

    public void surrender(Color color){
        endGameFlag(color);
    }

    public Color getPuzzlePlayerColor(){
        return puzzlePlayerColor;
    }

    public int[][] translateBoard(ChessPiece[][] board,Color color){
        int[][] resultBoard=new int[8][8];

        for (int i = 0; i < resultBoard.length; i++) {
            for (int j = 0; j < resultBoard[i].length; j++) {
                if(board[i][j]==null)
                    continue;

                resultBoard[i][j]=board[i][j].getIdFromType();
            }
        }

        if(color==Color.BLACK)
            return rotateleft(rotateleft(resultBoard));

        return resultBoard;
    }

    private int[][] rotateleft(int[][] toRotate) {

		int[][] rotated = new int[toRotate[0].length][toRotate.length];

		for (int i = 0; i < toRotate.length; i++) {
			for (int j = toRotate[i].length - 1; j >= 0; j--) {
				rotated[j][i] = toRotate[i][toRotate[i].length - 1 - j];
			}
		}
		return rotated;
	}

    public int[][] translateColorBoard(ChessPiece[][] board,Color color){
        int[][] resultBoard=new int[8][8];

        for (int i = 0; i < resultBoard.length; i++) {
            for (int j = 0; j < resultBoard[i].length; j++) {
                if(board[i][j]==null)
                    continue;

                resultBoard[i][j]=board[i][j].getColor().getId();
            }
        }

        if(color==Color.BLACK)
            return rotateleft(rotateleft(resultBoard));

        return resultBoard;
    }

    public int[][] getKingBoard(Color kingsColor,Color color){
        int[][] resultBoard=new int[8][8];

        if(isKingUnderAttack(kingsColor,chessBoard)==null)
            return resultBoard;

        int[] kingsCoords=getKingPos(kingsColor,chessBoard);

        resultBoard[kingsCoords[0]][kingsCoords[1]]=1;

        zuege.get(zuege.size()-1).specialEvent+="+";

        if(color==Color.BLACK)
            return rotateleft(rotateleft(resultBoard));

        return resultBoard;
    }

    public int[][] getLastMove(Color color){
        int[][] resultBoard=new int[8][8];

        if(zuege.size()==0)
            return resultBoard;

        ChessOperation lastMove=zuege.get(zuege.size()-1);

        resultBoard[lastMove.x][lastMove.y]=1;

        resultBoard[lastMove.newX][lastMove.newY]=2;

        if(color==Color.BLACK)
            return rotateleft(rotateleft(resultBoard));

        return resultBoard;
    }

    public int[][] getBauerTransformEvent(Color color){
        int[][] resultBoard=new int[8][8];

        int[] bauerCoords=getBauerToTransform();

        if(bauerCoords==null)
            return resultBoard;

        resultBoard[bauerCoords[0]][bauerCoords[1]]=1;

        if(color==Color.BLACK)
            return rotateleft(rotateleft(resultBoard));

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

    public long getCurrentTime(Color color){

        long resultTime;

        if(color==currentPlayer){
            resultTime = getTimeLong(color)-(System.currentTimeMillis()-intervallStart);
            if(resultTime<0&&gameType == ChessGameType.PVP)
                endGameFlag(color);
        }else{
            resultTime = getTimeLong(color);
            if(resultTime<0  && gameType == ChessGameType.PVP )
                endGameFlag(color);
        }

        return resultTime;
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
        return bauerTransform;
    }


    //EndCondition

    private int[] getKingPos(Color kingsColor, ChessPiece[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                ChessPiece currentPiece=getPieceOn(i, j, board);

                if(currentPiece==null)
                    continue;

                if(currentPiece.getColor()==kingsColor&&currentPiece.getType()==ChessPieceType.KOENIG)
                    return new int[]{i,j};
            }
        }

        return null;
    }

    private int[] isKingUnderAttack(Color kingsColor,ChessPiece[][] board) {
        int[] kingPos=getKingPos(kingsColor,board);

        if(kingPos==null)
            return null;

        return isPositionUnderAttack(kingPos[0], kingPos[1], kingsColor, board);
    }

    private boolean isKingCheckmate(Color kingsColor){

        int[] attackerCoords=isKingUnderAttack(kingsColor,chessBoard);

        if(attackerCoords==null)
            return false;

        int[] kingPos=getKingPos(kingsColor,chessBoard);
        
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

        int[] kingsCoords=getKingPos(kingsColor,board);

        ChessPiece currentKing=getPieceOn(kingsCoords[0], kingsCoords[1], board);

        List<int[]> validAttackerCoords=validCoordsOf(attackerX, attackerY, board);
        validAttackerCoords.add(new int[]{attackerX,attackerY});

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(!isPieceOn(i, j, currentKing.getColor(), board))
                    continue;
                
                ChessPiece currentPiece=getPieceOn(i, j, board);

                if(currentPiece.getType()==ChessPieceType.KOENIG&&currentPiece.getColor()==kingsColor)
                    continue;

                List<int[]> currentValidCoords=validCoordsOf(i, j, board);

                for (int k = 0; k < validAttackerCoords.size(); k++) {
                    for (int l = 0; l < currentValidCoords.size(); l++) {
                        int[] currentAlliedCoords=currentValidCoords.get(l);

                        if(!doesListContainCoords(currentAlliedCoords[0], currentAlliedCoords[1], validAttackerCoords))
                            continue;

                        ChessPiece[][] testBoard=createNextBoard(i, j, currentAlliedCoords[0], currentAlliedCoords[1],board);

                        if(isKingUnderAttack(kingsColor, testBoard)==null)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public ChessPiece[][] createNextBoard(int x, int y, int gotoX, int gotoY,ChessPiece[][] board){

        ChessPiece[][] nextBoard=copyBoard(board);

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

                if(currentPiece.getType()==ChessPieceType.BAUER){
                    List<int[]> bauerValidAttackCoords=new ArrayList<>();

                    for (int k = 0; k < currentValidEnemyCoords.size(); k++) {
                        int[] currentCoord=currentValidEnemyCoords.get(k);

                        if(currentCoord[0]==i||currentCoord[1]!=j)
                            bauerValidAttackCoords.add(currentCoord);
                    }
                    currentValidEnemyCoords=bauerValidAttackCoords;
                }
                
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

    public int getWinner() {
        if(isRemis)
            return 3;

        if(winner==Color.WHITE)
            return 1;

        if(winner==Color.BLACK)
            return 2; 
        
        return 0;
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

    private ChessPiece[][] getBoardWOKing(Color kingToRemove,ChessPiece[][] board){

        int[] kingToRemoveCoords=getKingPos(kingToRemove,board);

        ChessPiece[][] resultBoard=copyBoard(board);

        resultBoard[kingToRemoveCoords[0]][kingToRemoveCoords[1]]=null;

        return resultBoard;
    }

    private ChessPiece[][] copyBoard(ChessPiece[][] boardToCopy){
        ChessPiece[][] copiedBoard=new ChessPiece[8][8];

        for (int i = 0; i < copiedBoard.length; i++) {
            for (int j = 0; j < copiedBoard[i].length; j++) {
                if(boardToCopy[i][j]==null)
                    continue;

                copiedBoard[i][j]=copyPiece(boardToCopy[i][j]);
            }
        }

        return copiedBoard;
    }

    private ChessPiece copyPiece(ChessPiece pieceToCopy){
        
        ChessPiece copiedPice=new ChessPiece(pieceToCopy.getIdFromType(), pieceToCopy.getColor().getId());

        if(pieceToCopy.hasMoved()){
            copiedPice.setHasMoved(true);
        }else{
            copiedPice.setHasMoved(false);
        }

        return copiedPice;
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

    public boolean nextStep(int from,int to,Color color){

        if(color==Color.BLACK)
            return nextStep(7-((from-(from%10))/10),7-(from%10),7-((to-(to%10))/10),7-(to%10));

        return nextStep((from-(from%10))/10,from%10,(to-(to%10))/10,to%10);
    }

    public boolean nextStep(int x, int y, int gotoX, int gotoY){
        if(gameType==ChessGameType.PUZZLE)
            return nextPuzzleStep(x, y, gotoX, gotoY);

        if(isRemis)
            return false;

        long currentTime=getTimeLong(currentPlayer);

        if((currentTime-(System.currentTimeMillis()-intervallStart)<=0)&&gameType!=ChessGameType.PVE)
            endGameFlag(currentPlayer);

        if(winner!=null)
            return false;

        if(bauerTransform)
            return false;

        if(!moveIsValid(x, y, gotoX, gotoY))
            return false;

        movePiece(x, y, gotoX, gotoY, "");

        //kign can be slain without resistance next Turn SOLLTE NICHT EINTREFFEN
        if(isKingUnderAttack(currentPlayer,chessBoard)!=null)
            endGameFlag(currentPlayer);


        if(getBauerToTransform()!=null){
            bauerTransform=true;
            letzterZug=zuege.get(zuege.size()-1);
            zuege.remove(letzterZug);
        }else{
            if(gameType!=ChessGameType.PVE)
                timeManager();

            toggleCurrentPlayer();
        }

        //usavable Situation
        if(isKingCheckmate(currentPlayer)) {
            endGameFlag(currentPlayer);
            zuege.get(zuege.size()-1).specialEvent +="#";
        }


        isRemis=remisManager();

        if(gameType==ChessGameType.PVE) 
            doBotMove();

        return true;
    }

    private boolean doBotMove(){

        if(winner!=null)
            return false;

        int[] genratedMove=generateBotMove(Color.BLACK, gameDifficulty);

        movePiece(genratedMove[1], genratedMove[2], genratedMove[3], genratedMove[4],"");

        if(bauerTransform){
            return false;
        }

        if(getBauerToTransform()!=null){
            letzterZug=zuege.get(zuege.size()-1);
            zuege.remove(letzterZug);

            bauerTransform(5);
        }else{
            toggleCurrentPlayer();
        }

        //usavable Situation
        if(isKingCheckmate(currentPlayer)) {
            endGameFlag(currentPlayer);
            zuege.get(zuege.size()-1).specialEvent +="#";
        }

        return true;
    }

    public int[] getAssistance(){

        if(gameType==ChessGameType.PVE&&currentPlayer==Color.WHITE)
            return generateBotMove(currentPlayer, difficulty.HARD);
        
        
        if(currentPlayer==Color.BLACK){
            AssistanceCounter[1]++;
        }else{
            AssistanceCounter[0]++;
        }

        int[] botMove=generateBotMove(currentPlayer, difficulty.HARD);

        return currentPlayer==Color.WHITE?botMove:(new int[]{botMove[0],7-botMove[1],7-botMove[2],7-botMove[3],7-botMove[4]});
    }

    public int[] generateBotMove(Color color,difficulty difficulty){
        return new ChessBot().getBestMove(chessBoard, color, difficulty);
    }

    private boolean nextPuzzleStep(int x, int y, int gotoX, int gotoY){

        if(winner!=null)
            return false;

        if(bauerTransform)
            return false;

        int[] currentMove=puzzleMoves[zuege.size()];
        
        if(y!=(currentMove[0]-(currentMove[0]%10))/10||x!=currentMove[0]%10||gotoY!=(currentMove[1]-(currentMove[1]%10))/10||gotoX!=currentMove[1]%10)
            return false;

        movePiece(x, y, gotoX, gotoY,"");

        if(isKingUnderAttack(currentPlayer,chessBoard)!=null)
            endGameFlag(currentPlayer);

        if(getBauerToTransform()!=null)
            bauerTransform=true;

        if(isKingCheckmate(currentPlayer))
            endGameFlag(currentPlayer);

        toggleCurrentPlayer();

        if(currentPlayer!=puzzlePlayerColor)
            nextEnemyStep();

        return true;
    }

    private void nextEnemyStep(){

        if(puzzleMoves.length==zuege.size()){
            endGameFlag(currentPlayer);
            return;
        }

        int[] currentMove=puzzleMoves[zuege.size()];
        
        movePiece(currentMove[0]%10,(currentMove[0]-(currentMove[0]%10))/10,currentMove[1]%10,(currentMove[1]-(currentMove[1]%10))/10,"");

        toggleCurrentPlayer();
    }

    private boolean remisManager(){
        if(remisCounterWhite>=75||remisCounterBlack>=75)
            return true;

        if(requestRemisWhite&&requestRemisBlack&&remisCounterWhite+remisCounterBlack>=50)
            return true;

        return false;
    }

    public boolean getRemis(){
        return isRemis;
    }

    private void remisPatternManager(){

        if(remisPattern.isEmpty()){

            remisPatternStatus.put(remisPattern.size(), new Integer[]{0,enPassantSquare[0],enPassantSquare[1]});
            remisPattern.put(remisPattern.size(), copyBoard(chessBoard));
 
            return;
        }

        int match=-1;

        for (int i = 0; i < remisPattern.size(); i++) {
            if(euqualBoards(chessBoard, remisPattern.get(i))){

                if(remisPatternStatus.get(i)[1]==enPassantSquare[0]&&remisPatternStatus.get(i)[2]==enPassantSquare[1])
                    continue;

                if(!isKingMovesetEqual(Color.WHITE, chessBoard, remisPattern.get(i))||!isKingMovesetEqual(Color.BLACK, chessBoard, remisPattern.get(i)))
                    continue;
                
                match=i;
                break;
            }
        }

        if(match==-1){
            remisPatternStatus.put(remisPattern.size(), new Integer[]{0,enPassantSquare[0],enPassantSquare[1]});
            remisPattern.put(remisPattern.size(), copyBoard(chessBoard));
            
            return;
        }

        Integer[] status=remisPatternStatus.get(match);
        status[0]+=1;

        if(status[0]==5)
            isRemis=true;

        remisPatternStatus.remove(match);
        remisPatternStatus.put(match, status);
    
    }

    private boolean isKingMovesetEqual(Color kingscolor,ChessPiece[][] board1,ChessPiece[][] board2){
        int[] kingscoords=getKingPos(kingscolor,chessBoard);

        return isMovesetEqual(kingscoords[0], kingscoords[1], board1, board2);
    }

    private boolean isMovesetEqual(int x,int y,ChessPiece[][] board1,ChessPiece[][] board2){
        if(!isInBounds(x, y))
            return false;

        List<int[]> coordList1=validCoordsOf(x, y, board1);
        List<int[]> coordList2=validCoordsOf(x, y, board2);

        return areListsEqual(coordList1,coordList2);
    }

    private boolean areListsEqual(List<int[]> list1,List<int[]> list2){
        if((list1==null&&list2!=null)||(list1!=null&&list2==null))
            return false;

        if(list1==null&&list2==null)
            return true;

        if(list1.size()!=list2.size())
            return false;

        for (int i = 0; i < list1.size(); i++) {
            int[] currentCoord1=list1.get(i);
            int[] currentCoord2=list1.get(i);

            if(currentCoord1[0]!=currentCoord2[0]||currentCoord1[1]!=currentCoord2[1])
                return false;
        }

        return true; 
    }

    private boolean euqualBoards(ChessPiece[][] board1,ChessPiece[][] board2){
        if(board1==null||board2==null)
            return false;

        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[i].length; j++) {
                if(board1[i][j]==null&&board2[i][j]!=null)
                    return false;

                if(board1[i][j]==null)
                    continue;

                if(!board1[i][j].isEqual(board2[i][j]))
                    return false;
            }
        }

        return true;
    }

    /*private boolean compare2DArr(int[][] arr1, int[][] arr2){
        if(arr1==null||arr2==null)
            return false;

        if(arr1.length!=arr2.length)
            return false;

        for (int i = 0; i < arr1.length; i++) {
            if(arr1[i].length!=arr2[i].length)
                return false;
        }

        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[i].length; j++) {
                if(arr1[i][j]!=arr2[i][j])
                    return false;
            }
        }

        return true;
    }*/


    public void setRequestRemis(Color player,boolean value){
        if(player==Color.BLACK){
            requestRemisBlack=value;
        }else{
            requestRemisWhite=value;
        }
    }

    private void remisCounterManager(ChessPiece currentPiece,ChessPiece previousPiece){

        if(currentPiece.getColor()==Color.BLACK){
            if(currentPiece.getType()==ChessPieceType.BAUER){
                remisCounterBlack=0;
            }else if(previousPiece!=null){
                remisCounterBlack=0;
            }else{
                remisCounterBlack++;
            }

        }else{
            if(currentPiece.getType()==ChessPieceType.BAUER){
                remisCounterWhite=0;
            }else if(previousPiece!=null){
                remisCounterWhite=0;
            }else{
                remisCounterWhite++;
            }
        }
    }

    public boolean bauerTransform(int id){
        if(!bauerTransform||id>5||id<2)
            return false;

        int[] currentBauerCoords=getBauerToTransform();

        ChessPiece currentPiece=getPieceOn(currentBauerCoords[0], currentBauerCoords[1], chessBoard);

        if(currentPiece==null)
            return false;

        chessBoard[currentBauerCoords[0]][currentBauerCoords[1]]=new ChessPiece(id, currentPiece.getColor().getId());

        timeManager();
        toggleCurrentPlayer();

       isKingUnderAttack(getEnemyColorOf(currentPiece), chessBoard);
            letzterZug.specialEvent+="="+letzterZug.movingPiece.getType();


        zuege.add(letzterZug);

        bauerTransform=false;

        if(gameType==ChessGameType.PVE) 
            doBotMove();
            
        return true;
    }

    private int[] getBauerToTransform(){

        for (int i = 0; i < chessBoard[0].length; i++) {
            ChessPiece currentPiece=getPieceOn(0, i, chessBoard);

            if(currentPiece==null||currentPiece.getColor()==Color.BLACK||currentPiece.getType()!=ChessPieceType.BAUER)
                continue;

            return new int[]{0,i};
        }

        for (int i = 0; i < chessBoard[7].length; i++) {
            ChessPiece currentPiece=getPieceOn(7, i, chessBoard);

            if(currentPiece==null||currentPiece.getColor()==Color.WHITE||currentPiece.getType()!=ChessPieceType.BAUER)
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

        validCoordsOfcurrentPiece=testMovesForCheckMate(x, y,chessBoard, validCoordsOfcurrentPiece);

        return doesListContainCoords(gotoX, gotoY, validCoordsOfcurrentPiece);
    }


    private void movePiece(int x, int y, int gotoX, int gotoY, String specialEvent){

        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);

        ChessPiece preveiousPiece=getPieceOn(gotoX, gotoY, chessBoard);

        //istKleineRochade
        if((gotoY-y>1)&&currentPiece.getType()==ChessPieceType.KOENIG){
            ChessPiece turm=getPieceOn(x, y+3,chessBoard);
            turm.sethasMovedTrue(zuege.size());
            specialEvent = "O-O";
            chessBoard[x][y+3]=null;
            chessBoard[x][y+1]=turm;
        }

        //istGroßeRochade
        if((y-gotoY>1)&&currentPiece.getType()==ChessPieceType.KOENIG){
            ChessPiece turm=getPieceOn(x, y-4,chessBoard);
            turm.sethasMovedTrue(zuege.size());
            specialEvent = "O-O-O";
            chessBoard[x][y-4]=null;
            chessBoard[x][y-1]=turm;
        }   
        
        //istEnPassant
        if(isEnPassantSquare(gotoX, gotoY)&&currentPiece.getType()==ChessPieceType.BAUER){
            int movingDirection;

            if(currentPiece.getColor()==Color.BLACK){
                movingDirection=1;
            }else{
                movingDirection=-1;
            }

            preveiousPiece=getPieceOn(gotoX-movingDirection, gotoY, chessBoard);
            chessBoard[gotoX-movingDirection][gotoY]=null;
        }

        enPassantSquare=new int[]{-1,-1};

        //istZweierBauerMove
        if(currentPiece.getType()==ChessPieceType.BAUER&&y-gotoY==0&&(x-gotoX==2||x-gotoX==-2)){
            int movingDirection;

            if(currentPiece.getColor()==Color.BLACK){
                movingDirection=1;
            }else{
                movingDirection=-1;
            }

            enPassantSquare=new int[]{x+movingDirection,y,currentPiece.getColor().getId()};
        }

        chessBoard[x][y]=null;
        chessBoard[gotoX][gotoY]=currentPiece;

        ChessOperation currentOperation=new ChessOperation(x, y, gotoX, gotoY, currentPiece, preveiousPiece, specialEvent);
        zuege.add(currentOperation);

        currentPiece.sethasMovedTrue(zuege.size());

        remisCounterManager(currentPiece, preveiousPiece);
        remisPatternManager();
    }

    private void testMovePieceOnBoard(int x, int y, int gotoX, int gotoY,ChessPiece[][]board){
        ChessPiece currentPiece=getPieceOn(x, y, board);

        //istKleineRochade
        if((gotoY-y>1)&&currentPiece.getType()==ChessPieceType.KOENIG){
            ChessPiece turm=getPieceOn(x, y+3,board);
            turm.sethasMovedTrue(zuege.size());

            board[x][y+3]=null;
            board[x][y+1]=turm;
        }

        //istGroßeRochade
        if((y-gotoY>1)&&currentPiece.getType()==ChessPieceType.KOENIG){
            ChessPiece turm=getPieceOn(x, y-4,board);
            turm.sethasMovedTrue(zuege.size());

            board[x][y-4]=null;
            board[x][y-1]=turm;
        }   
        
        //istEnPassant
        if(isEnPassantSquare(gotoX, gotoY)&&currentPiece.getType()==ChessPieceType.BAUER){
            int movingDirection;

            if(currentPiece.getColor()==Color.BLACK){
                movingDirection=1;
            }else{
                movingDirection=-1;
            }

            board[gotoX-movingDirection][gotoY]=null;
        }

        board[x][y]=null;
        board[gotoX][gotoY]=currentPiece;
    }

    //ValidMoves

    public ChessPiece[][] getAllCoords(ChessPiece[][] board,Color color){
        ChessPiece[][] resultBoard=copyBoard(board);

        for (int i = 0; i < resultBoard.length; i++) {
            for (int j = 0; j < resultBoard[i].length; j++) {
                if(resultBoard[i][j]==null)
                    continue;

                if(resultBoard[i][j].getColor()!=color)
                    continue;

                List<int[]> potentiallyValidCoords = validCoordsOf(i, j, resultBoard);

                resultBoard[i][j].validMoves=testMovesForCheckMate(i, j,board, potentiallyValidCoords);
                
            }
        }

        return resultBoard;
    }

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

                if(!currentChessPiece.hasMoved()){
                    int[] kleineRohade=kleineRochade(currentChessPiece.getColor(),board);
                    if(kleineRohade!=null)
                        resultValidCoords.add(kleineRohade);

                    int[] großeRohade=großeRochade(currentChessPiece.getColor(),board);
                    if(großeRohade!=null)
                        resultValidCoords.add(großeRohade);
                }

                ChessPiece[][] boardWOcurrentKing=getBoardWOKing(currentChessPiece.getColor(),board);

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

    private int[] kleineRochade(Color kingsColor,ChessPiece[][] board){
        int[] kingsCoords=getKingPos(kingsColor,board);

        ChessPiece king=getPieceOn(kingsCoords[0], kingsCoords[1], board);

        if(king==null||king.hasMoved())
            return null;

        ChessPiece turm=getPieceOn(kingsCoords[0], kingsCoords[1]+3,board);

        if(turm==null||turm.getColor()!=kingsColor||turm.hasMoved())
            return null;

        if(isPieceOn(kingsCoords[0], kingsCoords[1]+1, board)||isPieceOn(kingsCoords[0], kingsCoords[1]+2, board))
            return null;

        return new int[]{kingsCoords[0], kingsCoords[1]+2};
    }

    private int[] großeRochade(Color kingsColor,ChessPiece[][] board){
        int[] kingsCoords=getKingPos(kingsColor,board);

        ChessPiece king=getPieceOn(kingsCoords[0], kingsCoords[1], board);

        if(king==null||king.hasMoved())
            return null;

        ChessPiece turm=getPieceOn(kingsCoords[0], kingsCoords[1]-4,board);

        if(turm==null||turm.getColor()!=kingsColor||turm.hasMoved())
            return null;

        if(isPieceOn(kingsCoords[0], kingsCoords[1]-1, board)||isPieceOn(kingsCoords[0], kingsCoords[1]-2, board)||isPieceOn(kingsCoords[0], kingsCoords[1]-3, board))
            return null;

        return new int[]{kingsCoords[0], kingsCoords[1]-2};
    }

    private List<int[]> testMovesForCheckMate(int x,int y,ChessPiece[][] board,List<int[]> potentiallyValidMoves){
        
        ChessPiece currentPiece=getPieceOn(x, y, board);

        if(currentPiece==null)
            return null;
        
        List<int[]> validResultMoves=new ArrayList<>();

        ChessPiece[][] testBoard;

        for (int i = 0; i < potentiallyValidMoves.size(); i++) {
            int[] currentPos=potentiallyValidMoves.get(i);

            testBoard=copyBoard(board);

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

            if(!isPieceOn(currentx, currenty,currentPiece.getColor(),board))
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
            movingDirection=1;
        }else{
            movingDirection=-1;
        }

        if(!isPieceOn(x+movingDirection, y, board)&&isInBounds(x+movingDirection, y)){

            resultValidCoords.add(new int[]{x+movingDirection,y});

            if(currentPiece.hasMoved()==false){

                if(isInBounds(x+movingDirection*2, y)){
                    if(!isPieceOn(x+movingDirection*2, y, board)){
                        resultValidCoords.add(new int[]{x+movingDirection*2,y});
                    }
                }   
            }
        }

        if(isPieceOn(x+movingDirection, y+1,getEnemyColorOf(currentPiece), board)||(isEnPassantSquare(x+movingDirection, y+1)&&enPassantSquare[2]!=currentPiece.getColor().getId()))
            resultValidCoords.add(new int[]{x+movingDirection,y+1});

        if(isPieceOn(x+movingDirection, y-1,getEnemyColorOf(currentPiece), board)||(isEnPassantSquare(x+movingDirection, y-1)&&enPassantSquare[2]!=currentPiece.getColor().getId()))
        resultValidCoords.add(new int[]{x+movingDirection,y-1});

        return resultValidCoords;
    }

    private boolean isEnPassantSquare(int x,int y){
        if(!isInBounds(x, y))
            return false;

        if(enPassantSquare==null)
            return false;

        return x==enPassantSquare[0]&&y==enPassantSquare[1];
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

    public int[][] exportMoves(int x, int y,ChessPiece[][] board,Color currentPlayerColor){

        ChessPiece currentPiece=getPieceOn(x, y, board);

        if(currentPiece==null||currentPiece.getColor()!=currentPlayerColor)
            return null;

        List<int[]> validCoords=validCoordsOf(x, y, board);

        //validCoords=testMovesForCheckMate(x, y, chessBoard,validCoords);

        return fillCoordsIntoArray(validCoords);
    }

    public int[][] checkedGetHighlightOf(int x, int y,Color color){

        ChessPiece currentPiece=getPieceOn(x, y, chessBoard);

        if(currentPiece==null||currentPiece.getColor()!=currentPlayer)
            return null;
        
        List<int[]> validCoords=validCoordsOf(x, y, chessBoard);

        validCoords=testMovesForCheckMate(x, y, chessBoard,validCoords);

        if(color==Color.BLACK)
            return rotateleft(rotateleft(fillCoordsIntoArray(validCoords)));

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
            result+=Integer.toString(8-i)+"  ";
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
            result+=Integer.toString(8-i)+"  ";
            result+="\n";
        }

        result+="    "+"      A      "+"      B      "+"      C      "+"      D      "+"      E      "+"      F      "+"      G      "+"     H     \n";
        return result;
    }

    public String pgnList() {
        StringBuilder pgn = new StringBuilder();
        int zugNummer = 1;
        for (int i = 0; i < zuege.size(); i++) {
            String zug = zuege.get(i).toStringKomprimiert();
            pgn.append(zugNummer).append(". ");
            pgn.append(zug).append(" ");
            if (i + 1 < zuege.size()) {
                pgn.append(zuege.get(i + 1).toStringKomprimiert()).append(" ");
                i++;
                zugNummer++;
            } else {
                return pgn.toString();
            }
        }
        return pgn.toString();
    }
}
