package com.ChesSEP.ChesSEP.Friendlist;

import lombok.Value;
import org.springframework.http.ResponseEntity;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
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
        friendService.sendFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.acceptFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/cancelFriendRequest")
    public void cancelFriendRequest(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.cancelFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/denyFriendRequest")
    public void denyFriendRequest(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.denyFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/getMyFriendlist")
    public ResponseEntity<UserRequestHolder[]> getMyFriendlist(@RequestHeader(value = "Authorization") String jwtToken){
        return ResponseEntity.ok(friendService.getMyFriendlist(jwtToken));
    }

    @PostMapping("/getFriendlistOf")
    public ResponseEntity<UserRequestHolder[]> getFriendlistOf(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder target){
        return ResponseEntity.ok(friendService.getFriendlistOf(jwtToken, target));
    }

    @PostMapping("/deleteFriend")
    public void deleteFriend(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody UserRequestHolder Friend){
        friendService.deleteFriend(jwtToken, Friend.getId());
    }
}
