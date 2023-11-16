package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchmakingService {

    private final ChessgameRepository chessgameRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    //public boolean test;
    //zum Testen der Methode acceptMatchRequest

    public List<ChessGame> onGoingGame=new ArrayList<ChessGame>();

    public Queue<Long> matchmaking=new LinkedList<Long>();

    private User getUserFromToken(String jwtToken){
        return userRepository.findByEmail(tokenService.extractEmail(jwtToken.substring(7)));
    }

    public void queueMatch(String jwtToken){
        User sender=getUserFromToken(jwtToken);

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

    public void dequeueMatch(String jwtToken){
        User sender=getUserFromToken(jwtToken);

        if(!matchmaking.contains(sender.getId())){
            return;
        }
        matchmaking.remove(sender.getId());

    }

    public void requestMatch(String jwtToken, String friendemail){
        User sender=getUserFromToken(jwtToken);
        long friendId=userRepository.findByEmail(friendemail).getId();

        if(matchRequestRepository.getRequest(sender.getId(), friendId) != null){
            return;
        }
        matchRequestRepository.save(MatchRequest.builder()
                .matchRequestID(new MatchRequestID(sender.getId(), friendId))
                .build());
    }

    public void acceptMatchRequest(String jwtToken, Long friendId){
        User sender=getUserFromToken(jwtToken);
        MatchRequest request = matchRequestRepository.getRequest(sender.getId(), friendId);

        if(request != null){
            dequeueMatch(jwtToken);
            startMatch(sender.getId(), friendId,
                    sender.getVorname()+"vs"+userRepository.findUserById(friendId).getVorname(), 5L);
            matchRequestRepository.delete(request);
            //test = true;
            return;
        }
            //test = false;
    }

    public void denyMatchRequest(String jwtToken){
        User user=getUserFromToken(jwtToken);
        MatchRequest request=matchRequestRepository.getRequestWith(user.getId());

        if(request == null){
            return;
        }
        matchRequestRepository.delete(request);
    }

    public ChessGame getMyCurrentMatch(String jwtToken){
        User user=getUserFromToken(jwtToken);

        List<ChessGame> result;

        result=onGoingGame.stream()
            .filter((game)->(game.getPlayerBlackID()==user.getId()||game.getPlayerWhiteID()==user.getId()))
            .collect(Collectors.toList());

        if(onGoingGame.size() == 0)
            return null;

        return result.get(0);
    }

    public UserRequestHolder[] getMyMatchInvitations(String jwtToken){
        User user=getUserFromToken(jwtToken);

        List<MatchRequest> list = matchRequestRepository.searchInvited(user.getId());
        UserRequestHolder[] arr = new UserRequestHolder[list.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = UserRequestHolder.builder()
                    .id(userRepository.findUserById(list.get(i).getMatchRequestID().RequestorID).getId())
                    .vorname(userRepository.findUserById(list.get(i).getMatchRequestID().RequestorID).getVorname())
                    .nachname(userRepository.findUserById(list.get(i).getMatchRequestID().RequestorID).getNachname())
                    .build();
        }
        return arr;
    }

    public UserRequestHolder getMyMatchRequest(String jwtToken){
        MatchRequest request=matchRequestRepository.searchRequest(getUserFromToken(jwtToken).getId());

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

        onGoingGame.add(thisGame);
    }

    public void endMyMatch(String jwtToken){
        User user=getUserFromToken(jwtToken);

        ChessGame game=onGoingGame.stream()
            .filter(a->a.getPlayerBlackID()==user.getId()||a.getPlayerWhiteID()==user.getId())
            .collect(Collectors.toList()).get(0);

        onGoingGame.remove(game);
    }
}
