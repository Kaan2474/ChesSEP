package com.ChesSEP.ChesSEP.Friendlist;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import com.ChesSEP.ChesSEP.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;


    private User getUserFromToken(String jwtToken){
        return userRepository.findByEmail(tokenService.extractEmail(jwtToken.substring(7)));
    }
    public void sendFriendRequest(String jwtToken, Long FriendId){
        if(friendRepository.secondRequest(getUserFromToken(jwtToken).getId(), FriendId) == null &&
           friendRepository.isFriend(getUserFromToken(jwtToken).getId(), FriendId) == null){
            friendRepository.save(Friend.builder()
                    .friendID(new FriendID(getUserFromToken(jwtToken).getId(), FriendId))
                    .type(FriendTyp.REQUEST)
                    .build());
        }
        else if(friendRepository.secondRequest(getUserFromToken(jwtToken).getId(), FriendId) != null &&
                friendRepository.isFriend(getUserFromToken(jwtToken).getId(), FriendId) == null){
            friendRepository.save(Friend.builder()
                    .friendID(new FriendID(getUserFromToken(jwtToken).getId(), FriendId))
                    .type(FriendTyp.FRIEND)
                    .build());
        }
    }
    public void acceptFriendRequest(String jwtToken, Long FriendId){
        if(friendRepository.secondRequest(getUserFromToken(jwtToken).getId(), FriendId) != null &&
                friendRepository.isFriend(getUserFromToken(jwtToken).getId(), FriendId) == null){
            friendRepository.save(Friend.builder()
                    .friendID(new FriendID(getUserFromToken(jwtToken).getId(), FriendId))
                    .type(FriendTyp.FRIEND)
                    .build());
        }
    }
    public void cancelFriendRequest(String jwtToken, Long FriendId){
        friendRepository.delete(friendRepository.searchRequest(getUserFromToken(jwtToken).getId(), FriendId));
    }
    public void denyFriendRequest(String jwtToken, Long FriendId){
        friendRepository.delete(friendRepository.searchRequest(getUserFromToken(jwtToken).getId(), FriendId));
    }
    public UserRequestHolder[] getMyFriendlist (String jwtToken){
        return null;
    }
    public UserRequestHolder[] getMyFriendlistOf (String jwtToken, UserRequestHolder target){
        return null;
    }
    public void deleteFriend(String jwtToken, Long FriendId){
        friendRepository.delete(friendRepository.secondRequest(getUserFromToken(jwtToken).getId(), FriendId));
    }
}
