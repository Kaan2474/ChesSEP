package com.ChesSEP.ChesSEP.ChessGame;

import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    @GetMapping("/queueMatch")
    public void queueMatch(){
        matchmakingService.queueMatch();
    }

    @GetMapping("/dequeueMatch")
    public void dequeueMatch(){
        matchmakingService.dequeueMatch();
    }

    @PostMapping("/requestMatch")
    public void requestMatch(@RequestBody UserRequestHolder Friend){
        matchmakingService.requestMatch(Friend.getEmail());
    }

    @GetMapping("/denyMatchRequest")
    public void denyMatch(){
        matchmakingService.denyMatchRequest();
    }

    @GetMapping("/cancelMatchRequest")
    public void cancelMatch(){
        matchmakingService.cancelMatchRequest();
    }

    @PostMapping("/acceptMatchRequest")
    public void acceptMatchRequest(@RequestBody UserRequestHolder Friend){
        matchmakingService.acceptMatchRequest(Friend.getId());
    }

    @GetMapping("/getMyCurrentMatch")
    public ResponseEntity<ChessGame> getMyCurrentMatchID(){
        return ResponseEntity.ok(matchmakingService.getMyCurrentMatch());
    }

    @GetMapping("/getMyMatchInvitations")
    public ResponseEntity<UserRequestHolder[]> getMyMatchInvitations(){
        return ResponseEntity.ok(matchmakingService.getMyMatchInvitations());
    }

    @GetMapping("/getMyMatchRequest")
    public ResponseEntity<UserRequestHolder> getMyMatchRequest(){
        
        return ResponseEntity.ok(matchmakingService.getMyMatchRequest());
    }

    @GetMapping("/getMyCurrentEnemy")
    public ResponseEntity<Long> getMyMatchEnemy(){
        return ResponseEntity.ok(matchmakingService.getMyCurrentEnemy());
    }

    @GetMapping("/surrender")
    public void  surrender(){
        matchmakingService.surrender();
    }

    @GetMapping("/getMyCurrentFrame/{frameID}")
    public ResponseEntity<int[][][]> getMyCurrentFrame(@PathVariable int frameID){
        return ResponseEntity.ok(matchmakingService.getMyCurrentFrame(frameID));
    }


    //z.B. x=1 y=2 als /12
    @GetMapping("engine/makeAMove/{from}/{to}")
    public ResponseEntity<Boolean> makeAMove(@PathVariable int from, @PathVariable int to){
        return ResponseEntity.ok(matchmakingService.makeAMove(from,to));
    }

    //id which chessPiece you want the pawn to transform to
    @GetMapping("engine/transformBauer/{id}")
    public ResponseEntity<Boolean> transformBauer(@PathVariable int id){
        return ResponseEntity.ok(matchmakingService.transformBauer(id));
    }

    //forTesting
    @GetMapping("engine/testBoard")
    public ResponseEntity<int[][][]>getTestBoard(){
        return ResponseEntity.ok(matchmakingService.getTestBoard());
    }

    @PostMapping("engine/getPuzzleInfo")
    public ResponseEntity<String[]>getChessPuzzleInfo(@RequestBody String fileContent) throws IOException{
        return ResponseEntity.ok(matchmakingService.getCSVFileInfo(fileContent));
    }

    @PostMapping("engine/startPuzzle/{id}")
    public void startPuzzle(@RequestBody String fileContent,@PathVariable int id) throws IOException{
        matchmakingService.startPuzzle(fileContent,id);
    }

    //Streaming
    @GetMapping("/getCurrentStreamingFrame/{gameId}/{userId}/{frameId}")
    public ResponseEntity<int[][][]> getCurrentStreamingFrame(@PathVariable long gameId, @PathVariable long userId, @PathVariable int frameId){
        return ResponseEntity.ok(matchmakingService.getCurrentStreamingFrame(frameId, gameId, userId));
    }

    @GetMapping("/allMatches")
    public ResponseEntity<List<ChessGame>> allMatches(){
        return ResponseEntity.ok(matchmakingService.allMatches());
    }
}
