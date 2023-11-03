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
    public void sendFriendRequest(String jwtToken, User Friend){

    }
    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(String jwtToken, User Friend){

    }
    @PostMapping("/cancelFriendRequest")
    public void cancelFriendRequest(String jwtToken, User Friend){

    }
    @PostMapping("/denyFriendRequest")
    public void denyFriendRequest(String jwtToken, User Friend){

    }
    @PostMapping("/getMyFriendlist")
    public ResponseEntity<UserRequestHolder[]> getMyFriendlist(String jwtToken){
        return null;
    }
    @PostMapping("/getMyFriendlistOf")
    public ResponseEntity<UserRequestHolder[]> getMyFriendlistOf(String jwtToken, UserRequestHolder target){
        return null;
    }
    @PostMapping("/deleteFriend")
    public void deleteFriend(String jwtToken, UserRequestHolder Friend){

    }
}
