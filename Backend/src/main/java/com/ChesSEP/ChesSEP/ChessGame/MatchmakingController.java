package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchmakingController {
    private final MatchmakingService matchmakingService;
    @PostMapping("/queueMatch")
    public void queueMatch(String jwtToken){

    }
    @PostMapping("/dequeueMatch")
    public void dequeueMatch(String jwtToken){

    }
    @PostMapping("/requestMatch")
    public void requestMatch(String jwtToken, UserRequestHolder Friend){

    }
    @PostMapping("/cancelMatch")
    public void cancelMatch(String jwtToken){

    }
    @PostMapping("/acceptMatchRequest")
    public void acceptMatchRequest(String jwtToken, UserRequestHolder Friend){

    }
    @PostMapping("/getMyCurrentMatchID")
    public ResponseEntity<String> getMyCurrentMatchID(String jwtToken){
        return null;
    }

    //  dummy
    //  public ResponseEntity<ChessGame> getMatch(String matchid) {}
    @PostMapping("/getMyMatchInvitations")
    public ResponseEntity<UserRequestHolder[]> getMyMatchInvitations(String jwtToken){
        return null;
    }
}
