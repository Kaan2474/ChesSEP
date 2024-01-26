package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.Privacy;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.ChesSEP.ChesSEP.CSVReader.CSVReader;
import com.ChesSEP.ChesSEP.ChessEngine.BoardManager;
import com.ChesSEP.ChesSEP.ChessEngine.ChessBoard;
import com.ChesSEP.ChesSEP.ChessEngine.ChessGameType;
import com.ChesSEP.ChesSEP.ChessEngine.Color;

import static com.ChesSEP.ChesSEP.ChessEngine.ChessGameType.PUZZLE;

@Service
@RequiredArgsConstructor
public class MatchmakingService {

    private final ChessgameRepository chessgameRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final UserRepository userRepository;
    private final PgnRepository pgnRepository;

    //public boolean test;
    //zum Testen der Methode acceptMatchRequest

    private final long matchLength=5L;

    public List<ChessGame> onGoingGame=new ArrayList<ChessGame>();

    public Queue<Long> matchmaking=new LinkedList<Long>();

    public Map<Long,BoardManager> boards=new HashMap<Long,BoardManager>();

    public CSVReader csvReader=new CSVReader();

    private User getSender(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void queueMatch(){
        User sender=getSender();

        if(matchmaking.contains(sender.getId()))
            return;

        MatchRequest request=matchRequestRepository.searchRequest(sender.getId());

        if(matchRequestRepository.searchRequest(sender.getId()) != null){
            matchRequestRepository.delete(request);
        }

        matchmaking.add(sender.getId());

        checkForMatch();
    }

    private void checkForMatch(){
        if(matchmaking.size()<2)
            return;

        User white=userRepository.findUserById(matchmaking.remove());
        User black=userRepository.findUserById(matchmaking.remove());
        
        startMatch(white.getId(), black.getId(), white.getVorname()+" "+white.getNachname()+" vs "+black.getVorname()+" "+black.getNachname(), matchLength);
    }

    public void dequeueMatch(){
        User sender=getSender();

        if(!matchmaking.contains(sender.getId())){
            return;
        }
        matchmaking.remove(sender.getId());

    }

    public void requestMatch(String friendemail){
        User sender=getSender();
        long friendId=userRepository.findByEmail(friendemail).getId();

        if(matchRequestRepository.getRequest(sender.getId(), friendId) != null){
            return;
        }
        matchRequestRepository.save(MatchRequest.builder()
                .matchRequestID(new MatchRequestID(sender.getId(), friendId))
                .build());
    }

    public void acceptMatchRequest(Long friendId){
        User sender=getSender();
        MatchRequest request = matchRequestRepository.getRequest(sender.getId(), friendId);

        if(request != null){
            dequeueMatch();
            startMatch(sender.getId(), friendId,
                    sender.getVorname()+"vs"+userRepository.findUserById(friendId).getVorname(), matchLength);
            matchRequestRepository.delete(request);
            //test = true;
            return;
        }
            //test = false;
    }

    public void denyMatchRequest(){
        User user=getSender();
        MatchRequest request=matchRequestRepository.getRequestWith(user.getId());

        if(request == null){
            return;
        }
        matchRequestRepository.delete(request);
    }

    public void cancelMatchRequest(){
        User user=getSender();
        MatchRequest request=matchRequestRepository.searchRequest(user.getId());

        if(request==null)
            return;

        matchRequestRepository.delete(request);
    }

    public ChessGame getMyCurrentMatch(){
        User user=getSender();

        List<ChessGame> result;

        result=onGoingGame.stream()
            .filter((game)->(game.getPlayerBlackID()==user.getId()||game.getPlayerWhiteID()==user.getId()))
            .collect(Collectors.toList());

        if(onGoingGame.size() == 0)
            return null;

        return result.get(0);
    }

    public Long getMyCurrentEnemy(){
        ChessGame game =getMyCurrentMatch();

        if(game==null)
            return null;

        User user=getSender();

        if(user.getId()==game.getPlayerBlackID()){
            return game.getPlayerWhiteID();
        }else{
            return game.getPlayerBlackID();
        }
    }

    public UserRequestHolder[] getMyMatchInvitations(){
        User user=getSender();

        List<MatchRequest> list = matchRequestRepository.searchInvited(user.getId());
        UserRequestHolder[] arr = new UserRequestHolder[list.size()];

        for (int i = 0; i < arr.length; i++) {
            User currentUser=userRepository.findUserById(list.get(i).getMatchRequestID().RequestorID);
            arr[i] = UserRequestHolder.builder()
                    .id(currentUser.getId())
                    .vorname(currentUser.getVorname())
                    .nachname(currentUser.getNachname())
                    .build();
        }
        return arr;
    }

    public UserRequestHolder getMyMatchRequest(){
        MatchRequest request=matchRequestRepository.searchRequest(getSender().getId());

        if(request==null)
            return null;

        User invited=userRepository.findUserById(request.matchRequestID.InvitedID);

        return UserRequestHolder.builder()
            .id(invited.getId())
            .vorname(invited.getVorname())
            .nachname(invited.getNachname())
            .email(invited.getEmail())
            .build();
    }

    public int[][][] getMyCurrentFrame(int frameID){
        User sender=getSender();
        ChessGame game=getMyCurrentMatch();
        Color thisPlayerColor;

        try {
            if (game.getPlayerBlackID() == sender.getId()) {
                thisPlayerColor = Color.BLACK;
            } else {
                thisPlayerColor = Color.WHITE;
            }
        }catch(NullPointerException e){
            return null;
        }

        BoardManager board=boards.get(game.getGameID());
        int[][][] frame;

        if((board.getManagedBoard().getZugId()==frameID&&frameID!= -1)&&!board.getManagedBoard().hasBauerToTransform()){
            frame=board.getOnlyMatchStatus();
        }else{
            frame=board.getMatchFrame(thisPlayerColor);
        }

        if(frame[0][2][0]==0)
            return frame;

        if(game.getPlayerBlackID()==sender.getId()){
            game.setBlackLastFrameSeen(true);
        }else{
            game.setWhiteLastFrameSeen(true);
        }    

        if(game.getPlayerBlackID()==-1L||game.getPlayerWhiteID()==-1L)
            endPuzzle();

        if(game.isBlackLastFrameSeen()&&game.isWhiteLastFrameSeen())
            endMyMatch();

        return frame;
    }

    public boolean makeAMove(int from, int to) {
        ChessGame game =getMyCurrentMatch();

        ChessBoard board=boards.get(game.getGameID()).getManagedBoard();

        if(game.getPlayerBlackID()==getSender().getId())
            return board.nextStep(from, to, Color.BLACK);

        return board.nextStep(from, to, Color.WHITE);
    }

    public boolean transformBauer(int id){
        ChessGame game =getMyCurrentMatch();

        ChessBoard board=boards.get(game.getGameID()).getManagedBoard();

        return board.bauerTransform(id);
    }

    public void startPVEMatch(int difficulty){

        User sender=getSender();

        Long time=System.currentTimeMillis();

        ChessGame newGame;

        newGame=ChessGame.builder()
            .playerWhiteID(sender.getId())
            .playerBlackID(-1L)
            .matchLength(-1L)
            .name(sender.getVorname()+" "+sender.getNachname()+"vs Bot")
            .blackLastFrameSeen(false)
            .whiteLastFrameSeen(false)
            .startTime(time)
            .type(ChessGameType.PVE)
            .result("")
        .build();

        chessgameRepository.save(newGame);

        ChessGame thisGame;

        thisGame=chessgameRepository.findGame(sender.getId(), -1L, time);

        BoardManager boardManager=new BoardManager();

        boardManager.startNewPVEMatch(difficulty, boardManager.getDefaultStartConfig());

        boards.put(thisGame.getGameID(), boardManager);

        onGoingGame.add(thisGame);
    }

    private void startMatch(Long playerWhite, Long playerBlack, String name, Long matchLength){

        Long time=System.currentTimeMillis();
        ChessGame newGame=ChessGame.builder()
            .playerWhiteID(playerWhite)
            .playerBlackID(playerBlack)
            .matchLength(matchLength)
            .name(name)
            .blackLastFrameSeen(false)
            .whiteLastFrameSeen(false)
            .startTime(time)
            .type(ChessGameType.PVP)
            .whiteElo(userRepository.findUserById(playerWhite).getElo())
            .blackElo(userRepository.findUserById(playerBlack).getElo())
            .result("")
        .build();

        chessgameRepository.save(newGame);

        ChessGame thisGame=chessgameRepository.findGame(playerWhite, playerBlack, time);

        boards.put(thisGame.getGameID(),new BoardManager());

        BoardManager thisBoard=boards.get(thisGame.getGameID());

        thisBoard.startNewMatch(matchLength,thisBoard.getDefaultStartConfig());

        onGoingGame.add(thisGame);
    }

    public void endMyMatch(){
        User user=getSender();

        ChessGame game=onGoingGame.stream()
            .filter(a->a.getPlayerBlackID()==user.getId()||a.getPlayerWhiteID()==user.getId())
            .collect(Collectors.toList()).get(0);

        onGoingGame.remove(game);

        BoardManager board=boards.get(game.getGameID());

        boards.remove(game.getGameID());

        int status=board.getManagedBoard().getWinner();

        switch (status) {
            case 1:
                User winner=userRepository.findUserById(game.getPlayerWhiteID());
                User loser=userRepository.findUserById(game.getPlayerBlackID());

                winner.setElo(winner.getElo()+10);
                loser.setElo(loser.getElo()-10);

                userRepository.save(winner);
                userRepository.save(loser);

                game.setResult("1-0");
                chessgameRepository.save(game);
                savePGN(game, board);

                break;
            case 2:
                winner=userRepository.findUserById(game.getPlayerBlackID());
                loser=userRepository.findUserById(game.getPlayerWhiteID());

                winner.setElo(winner.getElo()+10);
                loser.setElo(loser.getElo()-10);

                userRepository.save(winner);
                userRepository.save(loser);

                game.setResult("0-1");
                chessgameRepository.save(game);
                savePGN(game, board);

                break;
            default:

                game.setResult("1/2-1/2");
                chessgameRepository.save(game);
                savePGN(game, board);

                break;
        }
    }

    public void endMatchPVE(){
        User user=getSender();

        ChessGame game=onGoingGame.stream()
                .filter(a->a.getPlayerBlackID()==user.getId()||a.getPlayerWhiteID()==user.getId())
                .collect(Collectors.toList()).get(0);

        onGoingGame.remove(game);

        boards.remove(game.getGameID());


    }

    public int[][][] getTestBoard() {
        BoardManager boardManager=new BoardManager();
        boardManager.startNewMatch(200, boardManager.getDefaultStartConfig());

        return boardManager.getMatchFrame(Color.WHITE);
    }


    //ChessPuzzle

    public String[] getCSVFileInfo(String fileContent) throws IOException{
        String[] puzzles=csvReader.splitStringIntoPuzzles(fileContent);
        String[] puzzleInfo=csvReader.getPuzzleInfo(puzzles);

        return puzzleInfo;
    }

    public void startPuzzle(String fileContent, int id) throws IOException{
        String[] puzzles=csvReader.splitStringIntoPuzzles(fileContent);

        User sender=getSender();

        BoardManager boardManager=new BoardManager();
        boardManager.startNewChessPuzzle(csvReader.getStatus(id, puzzles), csvReader.CSVtoBoard(id, puzzles), csvReader.MovesToArr(id,puzzles));

        Color player=boardManager.getManagedBoard().getPuzzlePlayerColor();

        Long time=System.currentTimeMillis();

        ChessGame newGame;

        if(player==Color.WHITE){  
            newGame=ChessGame.builder()
                .playerWhiteID(sender.getId())
                .playerBlackID(-1L)
                .matchLength(-1L)
                .name("ChessPuzzle von"+sender.getVorname())
                .blackLastFrameSeen(false)
                .whiteLastFrameSeen(false)
                .startTime(time)
                .type(PUZZLE)
                .build();
        }else{
            newGame=ChessGame.builder()
                .playerWhiteID(-1L)
                .playerBlackID(sender.getId())
                .matchLength(-1L)
                .name("ChessPuzzle von"+sender.getVorname())
                .blackLastFrameSeen(false)
                .whiteLastFrameSeen(false)
                .startTime(time)
                .type(PUZZLE)
                .build();
        }

        chessgameRepository.save(newGame);

        ChessGame thisGame;

        if(player==Color.WHITE){
            thisGame=chessgameRepository.findGame(sender.getId(), -1L, time);
        }else{
            thisGame=chessgameRepository.findGame(-1L, sender.getId(), time);
        }

        boards.put(thisGame.getGameID(), boardManager);

        onGoingGame.add(thisGame);
    }

    public void surrender() {
        ChessGame game=getMyCurrentMatch();
        BoardManager board=boards.get(game.getGameID());


        if(game.getType() == ChessGameType.PVP) {
            savePGN(game, board);
            if (game.getPlayerBlackID() == getSender().getId()) {
                board.getManagedBoard().surrender(Color.BLACK);
            } else {
                board.getManagedBoard().surrender(Color.WHITE);
            }
        }

        switch (board.getManagedBoard().getGameType()){
            case 0:
                endMyMatch();
                break;
            case 1:
                endPuzzle();
                break;
            case 2:
                endMatchPVE();
                break;
        }


    }

    public void endPuzzle(){
        User sender=getSender();

        ChessGame game=getMyCurrentMatch();
        BoardManager board=boards.get(game.getGameID());

        Color thisPlayer;

        if(game.getPlayerWhiteID()==-1L){
            thisPlayer=Color.BLACK;
        }else{
            thisPlayer=Color.WHITE;
        }

        if(board.getManagedBoard().getWinner()==thisPlayer.getId()){
            sender.setCompleatedPuzzles(sender.getCompleatedPuzzles()+1);
            userRepository.save(sender);
        }

        boards.remove(game.getGameID());
        onGoingGame.remove(game);
        
    }

    //Streaming

    public int[][][] getCurrentStreamingFrame(int frameID, long gameID) {

       BoardManager board = boards.get(gameID);
        int[][][] frame;

        if(board != null){
            if ((board.getManagedBoard().getZugId() == frameID && frameID != -1) && !board.getManagedBoard().hasBauerToTransform()) {
                frame = board.getOnlyMatchStatus();
            } else {
                frame = board.streamingBoard(Color.WHITE);
            }
        }else{
            return null;
        }

        return frame;

    }

    private ChessGame liveMatch(long gameId) {
        ChessGame game = null;
        for (ChessGame x : onGoingGame) {
            if (x.getGameID() == gameId)
                game = x;
        }
        return game;
    }

    public List<ChessGame> allMatches(){
        List<ChessGame> streamingGames = new ArrayList<>();
        User white;
        User black;
        ChessGame game;

        for (int i = 0; i< onGoingGame.size(); i++) {
            game = onGoingGame.get(i);
            white = userRepository.findUserById(game.getPlayerWhiteID());
            black = userRepository.findUserById(game.getPlayerBlackID());

            if((game.getType() == ChessGameType.PVP) && white.getStreaming() == Privacy.OEFFENTLICH || black.getStreaming() == Privacy.OEFFENTLICH){
                streamingGames.add(game);
            }
        }
        return streamingGames;
    }

    //PGN

    private void savePGN(ChessGame game, BoardManager board){
        PGN pgnGame = PGN.builder()
                .pgnInfo(createPGNinfo(game, board.getManagedBoard()))
                .build();
        pgnRepository.save(pgnGame);

        game.setPgnId(pgnGame.getPgnId());
        chessgameRepository.save(game);
    }

    public String findPGNByPgnId(long pgnId){
        return pgnRepository.findPGNByPgnId(pgnId);
    }

    public String createPGNinfo(ChessGame chessGame, ChessBoard board){
        User white = userRepository.findUserById(chessGame.getPlayerWhiteID());
        User black = userRepository.findUserById(chessGame.getPlayerBlackID());
        return "[Event \""+chessGame.getName()+"\"]\n"+
               "[Site \"ChesSEP\"]\n"+
               "[Date \"??\"]\n"+
               "[Round \"-1\"]\n"+
               "[White \""+white.getVorname()+" "+white.getNachname()+"\"]\n"+
               "[Black \""+black.getVorname()+" "+black.getNachname()+"\"]\n"+
               "[Result \""+chessGame.getResult()+"\"]\n"+
               "\n"+board.pgnList()+chessGame.getResult()+"\n";
    }

}
