package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import com.ChesSEP.ChesSEP.ChessEngine.BoardManager;
import com.ChesSEP.ChesSEP.ChessEngine.ChessBoard;
import com.ChesSEP.ChesSEP.ChessEngine.Color;

@Service
@RequiredArgsConstructor
public class MatchmakingService {

    private final ChessgameRepository chessgameRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final UserRepository userRepository;

    //public boolean test;
    //zum Testen der Methode acceptMatchRequest

    public List<ChessGame> onGoingGame=new ArrayList<ChessGame>();

    public Queue<Long> matchmaking=new LinkedList<Long>();

    public Map<Long,BoardManager> boards=new HashMap<Long,BoardManager>();

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
        
        startMatch(white.getId(), black.getId(), white.getVorname()+" "+white.getNachname()+" vs "+black.getVorname()+" "+black.getNachname(), 5L);
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
                    sender.getVorname()+"vs"+userRepository.findUserById(friendId).getVorname(), 5L);
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

        ChessGame game =getMyCurrentMatch();

        Color thisPlayerColor;

        if(game.getPlayerBlackID()==sender.getId()){
            thisPlayerColor=Color.BLACK;
        }else{
            thisPlayerColor=Color.WHITE;
        }

        BoardManager board=boards.get(game.getGameID());

        if(board.getManagedBoard().getZugId()==frameID)
            return new int[0][0][0];

        int[][][] frame=board.getMatchFrame(thisPlayerColor);

        return frame;
    }

    public boolean makeAMove(int from, int to) {
        ChessGame game =getMyCurrentMatch();

        ChessBoard board=boards.get(game.getGameID()).getManagedBoard();

        return board.nextStep(from, to);
    }

    public boolean transformBauer(int id){
        ChessGame game =getMyCurrentMatch();

        ChessBoard board=boards.get(game.getGameID()).getManagedBoard();

        return board.transformBauer(id);
    }

    

    private void startMatch(Long playerWhite, Long playerBlack, String name, Long matchLength){

        Long time=System.currentTimeMillis();
        ChessGame newGame=ChessGame.builder()
                        .playerWhiteID(playerWhite)
                        .playerBlackID(playerBlack)
                        .matchLength(matchLength)
                        .name(name)
                        .startTime(time)
                .build();

        chessgameRepository.save(newGame);

        ChessGame thisGame=chessgameRepository.findGame(playerWhite, playerBlack, time);

        boards.put(thisGame.getGameID(),new BoardManager());

        BoardManager thisBoard=boards.get(thisGame.getGameID());

        thisBoard.startNewMatch(matchLength,10L, thisBoard.getDefaultStartConfig());

        onGoingGame.add(thisGame);
    }

    public void endMyMatch(){
        User user=getSender();

        ChessGame game=onGoingGame.stream()
            .filter(a->a.getPlayerBlackID()==user.getId()||a.getPlayerWhiteID()==user.getId())
            .collect(Collectors.toList()).get(0);

        onGoingGame.remove(game);
    }

    public int[][][] getTestBoard() {
        BoardManager boardManager=new BoardManager();
        boardManager.startNewMatch(200, 5L, boardManager.getDefaultStartConfig());

        return boardManager.getMatchFrame(Color.WHITE);
    }
}
