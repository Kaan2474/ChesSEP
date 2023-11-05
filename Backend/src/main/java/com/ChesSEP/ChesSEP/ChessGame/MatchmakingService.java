package com.ChesSEP.ChesSEP.ChessGame;



import com.ChesSEP.ChesSEP.Friendlist.Friend;
import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;

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
        if(matchmaking.contains(getUserFromToken(jwtToken).getId())){
            return;
        }
        matchmaking.add(getUserFromToken(jwtToken).getId());
    }

    public void dequeueMatch(String jwtToken){
        if(!matchmaking.contains(getUserFromToken(jwtToken).getId())){
            return;
        }
        matchmaking.remove(getUserFromToken(jwtToken).getId());

    }

    public void requestMatch(String jwtToken, UserRequestHolder Friend){
        if(matchRequestRepository.secondRequest(getUserFromToken(jwtToken).getId(), Friend.getId()) != null){
            return;
        }
        matchRequestRepository.save(MatchRequest.builder()
                .matchRequestID(new MatchRequestID(getUserFromToken(jwtToken).getId(), Friend.getId()))
                .build());
    }

    public void cancelMatchRequest(String jwtToken){
        if(matchRequestRepository.searchRequest(getUserFromToken(jwtToken).getId()) == null){
            return;
        }
        matchRequestRepository.delete(matchRequestRepository.searchRequest(getUserFromToken(jwtToken).getId()));
    }

    public void acceptMatchRequest(String jwtToken, UserRequestHolder Friend){
        if(matchRequestRepository.searchRequest(getUserFromToken(jwtToken).getId()) != null){
            startMatch(getUserFromToken(jwtToken).getId(), Friend.getId(),
                    getUserFromToken(jwtToken).getVorname()+"vs"+Friend.getVorname(), 5L);
        }
    }

    //  dummy
    //  public ChessGame getMatch(String matchid) {}


    public String getMyCurrentMatchID(String jwtToken){
        if(matchRequestRepository.searchRequest(getUserFromToken(jwtToken).getId()) == null){
            return null;
        }
        return String.valueOf(chessgameRepository.findGame(String.valueOf(getUserFromToken(jwtToken).getId())));
    }

    public UserRequestHolder[] getMyMatchInvitations(String jwtToken){
        List<MatchRequest> list = matchRequestRepository.searchInvited(getUserFromToken(jwtToken).getId());
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
