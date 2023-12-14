package com.ChesSEP.ChesSEP.ChessClub;


import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ChessClub")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ChessClubController {
    private final ChessClubService chessClubService;
    private ObjectMapper objectMapper=new ObjectMapper();

    @GetMapping("/leaveClub{chessclubId}")
    public void leaveClub(@PathVariable long chessclubId){
        chessClubService.leaveClub(chessclubId);
    }

    @GetMapping("/getAllChessClubs")
    public ChessClub[] getAllChessClubs(){
        return chessClubService.getAllChessClubs();
    }

    @GetMapping("/getMeinChessClubName")
    public ResponseEntity<String> getMeinChessClubName() throws JsonProcessingException{
        return ResponseEntity.ok(objectMapper.writeValueAsString(chessClubService.getMeinChessClubName()));
    }

    @GetMapping("/getChessClubOf/{userId}")
    public ResponseEntity<String> getChessClubOf(@PathVariable long userId) throws JsonProcessingException {
    String chessClub = chessClubService.getChessClubOf(userId);
        return new ResponseEntity<>(objectMapper.writeValueAsString(chessClub), HttpStatus.OK);
    }

    @GetMapping("/findChessClubById/{chessClubId}")
    public ResponseEntity<ChessClub> findChessClubById(@PathVariable long chessClubId){
        return new ResponseEntity<>(chessClubService.findChessClubById(chessClubId),HttpStatus.OK);
    }

    @GetMapping("/joinClubV2/{clubName}")
    public void joinClubV2(@PathVariable String clubName){
        chessClubService.joinClub(clubName);
    }

    @GetMapping("/createClubV2/{clubName}")
    public ResponseEntity<Boolean> createClubV2(@PathVariable String clubName){
        boolean check = chessClubService.createClubV2(clubName);
        if(check) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getMember/{id}")
    public ResponseEntity<UserRequestHolder[]> getMembers(@PathVariable long id){
        return ResponseEntity.ok(chessClubService.getChessClubMember(id));
    }
}
