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
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchmakingService {

    private final ChessgameRepository chessgameRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;


    public List<ChessGame> onGoingGame=new ArrayList<ChessGame>();

    public Queue<Long> matchmaking=new LinkedList<Long>();

    private User getUserFromToken(String jwtToken){
        return userRepository.findByEmail(tokenService.extractEmail(jwtToken.substring(7)));
    }

    public void queueMatch(String jwtToken){
        User sender=getUserFromToken(jwtToken);

        if(matchmaking.contains(sender.getId()) || matchRequestRepository.searchRequest(sender.getId()) != null){
            return;
        }

        if(matchmaking.isEmpty() || matchRequestRepository.searchRequest(sender.getId()) == null){
            matchmaking.add(sender.getId());
            return;
        }
        startMatch(sender.getId(), matchmaking.peek(), "", 5L);
        matchmaking.clear();
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

        if(matchRequestRepository.searchRequest(sender.getId()) != null){
            dequeueMatch(jwtToken);
            startMatch(sender.getId(), friendId,
                    sender.getVorname()+"vs"+userRepository.findUserById(friendId).getVorname(), 5L);
        }

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

        return result.get(0);
    }

    public UserRequestHolder[] getMyMatchInvitations(String jwtToken){
        User sender=getUserFromToken(jwtToken);

        List<MatchRequest> list = matchRequestRepository.searchInvited(sender.getId());
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
        MatchRequest request=matchRequestRepository.getRequestWith(getUserFromToken(jwtToken).getId());

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
        onGoingGame.add(ChessGame.builder()
                        .playerWhiteID(playerWhite)
                        .playerBlackID(playerBlack)
                        .matchLength(matchLength)
                        .name(name)
                .build());
    }

    private void endAndSaveMatch(ChessGame game){
        chessgameRepository.save(ChessGame.builder()
                        .gameID(game.getGameID())
                        .playerBlackID(game.getPlayerBlackID())
                        .playerWhiteID(game.getPlayerWhiteID())
                        .name(game.getName())
                        .matchLength(game.getMatchLength())
                .build());
        onGoingGame.remove(game);
    }
}
