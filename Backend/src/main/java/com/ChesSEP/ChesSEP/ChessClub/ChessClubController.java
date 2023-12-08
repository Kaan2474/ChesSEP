package com.ChesSEP.ChesSEP.ChessClub;


import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ChessClub[] getAllChessClubs(){
        return chessClubService.getAllChessClubs();
    }

    @GetMapping("/getMeinChessClubName")
    public String getMeinChessClubName(){
        return chessClubService.getMeinChessClubName();
    }


    @GetMapping("/joinClubV2/{clubName}")
    public void joinClubV2(@PathVariable String clubName){
        chessClubService.joinClubByMario(clubName);
    }
    @GetMapping("/createClubV2/{clubName}")
    public ResponseEntity<Boolean> createClubV2(@PathVariable String clubName){
        boolean check = chessClubService.createClubV2(clubName);
        if(check) {
            return new ResponseEntity<>(check, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }


    //Hier mit einer @PathVariable, vielleicht einfacher fürs Frontend
    @GetMapping("/getMember/{id}")
    public ResponseEntity<UserRequestHolder[]> getMembers(@PathVariable long id){
        return ResponseEntity.ok(chessClubService.getChessClubMember(id));
    }
}
