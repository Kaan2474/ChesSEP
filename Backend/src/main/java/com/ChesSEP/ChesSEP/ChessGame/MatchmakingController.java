package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class MatchmakingController {
    private final MatchmakingService matchmakingService;
    @PostMapping("/queueMatch")
    public void queueMatch(@RequestHeader(value = "Authorization") String jwtToken){
        matchmakingService.queueMatch(jwtToken);
    }
    @PostMapping("/dequeueMatch")
    public void dequeueMatch(@RequestHeader(value = "Authorization") String jwtToken){
        matchmakingService.dequeueMatch(jwtToken);
    }
    @PostMapping("/requestMatch")
    public void requestMatch(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        matchmakingService.requestMatch(jwtToken, Friend);
    }
    @PostMapping("/cancelMatch")
    public void cancelMatch(@RequestHeader(value = "Authorization") String jwtToken){
        matchmakingService.cancelMatchRequest(jwtToken);
    }
    @PostMapping("/acceptMatchRequest")
    public void acceptMatchRequest(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        matchmakingService.acceptMatchRequest(jwtToken, Friend);
    }
    @PostMapping("/getMyCurrentMatchID")
    public ResponseEntity<String> getMyCurrentMatchID(@RequestHeader(value = "Authorization") String jwtToken){
        return ResponseEntity.ok(matchmakingService.getMyCurrentMatchID(jwtToken));
    }

    //  dummy
    //  public ResponseEntity<ChessGame> getMatch(String matchid) {}
    @PostMapping("/getMyMatchInvitations")
    public ResponseEntity<UserRequestHolder[]> getMyMatchInvitations(@RequestHeader(value = "Authorization") String jwtToken){
        return ResponseEntity.ok(matchmakingService.getMyMatchInvitations(jwtToken));
    }
}
