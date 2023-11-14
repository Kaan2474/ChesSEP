package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    private List<ChessGame> onGoingGame;

    private Queue<Long> matchmaking;

    private User getUserFromToken(String jwtToken){
        return userRepository.findByEmail(tokenService.extractEmail(jwtToken.substring(7)));
    }

    public void queueMatch(String jwtToken){
        User sender=getUserFromToken(jwtToken);

        if(matchmaking.contains(sender.getId())){
            return;
        }

        if(matchmaking.isEmpty()){
            matchmaking.add(sender.getId());
            return;
        }
        startMatch(sender.getId(), matchmaking.peek(), "", 5L);
    }

    public void dequeueMatch(String jwtToken){
        User sender=getUserFromToken(jwtToken);

        if(!matchmaking.contains(sender.getId())){
            return;
        }
        matchmaking.remove(sender.getId());

    }

    public void requestMatch(String jwtToken, UserRequestHolder Friend){
        User sender=getUserFromToken(jwtToken);

        if(matchRequestRepository.getRequest(sender.getId(), Friend.getId()) != null){
            return;
        }
        matchRequestRepository.save(MatchRequest.builder()
                .matchRequestID(new MatchRequestID(sender.getId(), Friend.getId()))
                .build());
    }

    public void cancelMatchRequest(String jwtToken){
        User sender=getUserFromToken(jwtToken);

        if(matchRequestRepository.searchRequest(sender.getId()) == null){
            return;
        }
        matchRequestRepository.delete(matchRequestRepository.searchRequest(sender.getId()));
    }

    public void acceptMatchRequest(String jwtToken, UserRequestHolder Friend){
        User sender=getUserFromToken(jwtToken);

        if(matchRequestRepository.searchRequest(sender.getId()) != null){
            startMatch(sender.getId(), Friend.getId(),
                    sender.getVorname()+"vs"+Friend.getVorname(), 5L);
        }
    }

    public void denyMatchRequest(String jwtToken, UserRequestHolder friend){
        User user=getUserFromToken(jwtToken);
        MatchRequest request=matchRequestRepository.getRequest(user.getId(), friend.getId());

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
