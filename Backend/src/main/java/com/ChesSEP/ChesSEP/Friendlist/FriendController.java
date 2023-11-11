package com.ChesSEP.ChesSEP.Friendlist;

import org.springframework.http.ResponseEntity;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/sendFriendRequest")
    public void sendFriendRequest(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.sendFriendRequest(jwtToken, Friend.getEmail());
    }
    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.acceptFriendRequest(jwtToken, Friend.getId());
    }
    
    @PostMapping("/denyFriendRequest")
    public void denyFriendRequest(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.denyFriendRequest(jwtToken, Friend.getId());
    }
    @GetMapping("/getMyFriendlist")
    public ResponseEntity<UserRequestHolder[]> getMyFriendlist(@RequestHeader(value = "Authorization") String jwtToken){
        return ResponseEntity.ok(friendService.getMyFriendlist(jwtToken));
    }

    @PostMapping("/getFriendlistOf")
    public ResponseEntity<UserRequestHolder[]> getFriendlistOf(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder target){
        return ResponseEntity.ok(friendService.getFriendlistOf(jwtToken, target));
    }

    @GetMapping("/getMyPendingFriendRequests")
    public ResponseEntity<UserRequestHolder[]> getMyPendingFriendRequests(@RequestHeader(value = "Authorization")String jwtToken){
        return ResponseEntity.ok(friendService.getMyPendingFriendRequests(jwtToken));
    }

    @PostMapping("/deleteFriend")
    public void deleteFriend(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.deleteFriend(jwtToken, Friend.getId());
    }
}
