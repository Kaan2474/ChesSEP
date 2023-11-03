package com.ChesSEP.ChesSEP.Friendlist;

import org.springframework.http.ResponseEntity;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/sendFriendRequest")
    public void sendFriendRequest(String jwtToken, UserRequestHolder Friend){
        friendService.sendFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(String jwtToken, UserRequestHolder Friend){
        friendService.acceptFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/cancelFriendRequest")
    public void cancelFriendRequest(String jwtToken, UserRequestHolder Friend){
        friendService.cancelFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/denyFriendRequest")
    public void denyFriendRequest(String jwtToken, UserRequestHolder Friend){
        friendService.denyFriendRequest(jwtToken, Friend.getId());
    }
    @PostMapping("/getMyFriendlist")
    public ResponseEntity<UserRequestHolder[]> getMyFriendlist(String jwtToken){
        return ResponseEntity.ok(friendService.getMyFriendlist(jwtToken));
    }

    @PostMapping("/getFriendlistOf")
    public ResponseEntity<UserRequestHolder[]> getFriendlistOf(String jwtToken, UserRequestHolder target){
        return ResponseEntity.ok(friendService.getFriendlistOf(jwtToken, target));
    }

    @PostMapping("/deleteFriend")
    public void deleteFriend(String jwtToken, UserRequestHolder Friend){
        friendService.deleteFriend(jwtToken, Friend.getId());
    }
}
