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
    public ResponseEntity<Boolean> sendFriendRequest(@RequestBody UserRequestHolder Friend){
        return ResponseEntity.ok(friendService.sendFriendRequest(Friend.getEmail()));
    }
    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(@RequestBody UserRequestHolder Friend){
        friendService.acceptFriendRequest(Friend.getId());
    }
    
    @PostMapping("/denyFriendRequest")
    public void denyFriendRequest(@RequestBody UserRequestHolder Friend){
        friendService.denyFriendRequest(Friend.getId());
    }
    @GetMapping("/getMyFriendlist")
    public ResponseEntity<UserRequestHolder[]> getMyFriendlist(){
        return ResponseEntity.ok(friendService.getMyFriendlist());
    }

    @GetMapping("/getFriendlistOf/{id}")
    public ResponseEntity<UserRequestHolder[]> getFriendlistOf(@PathVariable String id){
        return ResponseEntity.ok(friendService.getFriendlistOf(Long.parseLong(id)));
    }

    @GetMapping("/getMyPendingFriendRequests")
    public ResponseEntity<UserRequestHolder[]> getMyPendingFriendRequests(){
        return ResponseEntity.ok(friendService.getMyPendingFriendRequests());
    }

    @PostMapping("/deleteFriend")
    public void deleteFriend(@RequestBody UserRequestHolder Friend){
        friendService.deleteFriend(Friend.getId());
    }
}
