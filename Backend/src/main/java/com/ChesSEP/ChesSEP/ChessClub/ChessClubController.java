package com.ChesSEP.ChesSEP.ChessClub;


import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ChessClub")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ChessClubController {
    private final ChessClubService chessClubService;

    @GetMapping("/getMembers")
    public ResponseEntity<UserRequestHolder[]> getMembers(@RequestBody String clubName){
        return ResponseEntity.ok(chessClubService.getMembers(clubName));
    }

    @PostMapping("/leaveClub")
    public void leaveClub(){
        chessClubService.leaveClub();
    }

    @PostMapping("/joinClub")
    public void joinClub(@RequestBody String clubName){
        chessClubService.joinClub(clubName);
    }

    @PostMapping("/createClub")
    public void createClub(@RequestBody String clubName){
        chessClubService.createClub(clubName);
    }

    @GetMapping("/getAllChessClubs")
    public List<ChessClub> getAllChessClubs(){
        return chessClubService.getAllChessClubs();
    }

}
