package com.ChesSEP.ChesSEP.Friendlist;

import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.Privacy;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import com.ChesSEP.ChesSEP.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        friendRepository.delete(friendRepository.searchRequest(FriendId, getUserFromToken(jwtToken).getId()));
    }
    public UserRequestHolder[] getMyFriendlist (String jwtToken){
        List<Friend> list = friendRepository.getFriendlist(getUserFromToken(jwtToken).getId());
        UserRequestHolder[] arr = new UserRequestHolder[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = UserRequestHolder.builder()
                    .id(userRepository.findUserById(list.get(i).getFriendID().FriendID1).getId())
                    .vorname(userRepository.findUserById(list.get(i).getFriendID().FriendID1).getVorname())
                    .nachname(userRepository.findUserById(list.get(i).getFriendID().FriendID1).getNachname())
                    .build();
        }
        return arr;
    }

    public UserRequestHolder[] getFriendlistOf (String jwtToken, UserRequestHolder target){
        if(userRepository.findUserById(target.getId()).getFriendlistPrivacy() == Privacy.PRIVAT){
                  return null;
        }
        List<Friend> anderelist = friendRepository.getFriendlist(target.getId());
        UserRequestHolder[] anderearr = new UserRequestHolder[anderelist.size()];
        for (int i = 0; i < anderearr.length; i++) {
            anderearr[i] = UserRequestHolder.builder()
                    .id(userRepository.findUserById(anderelist.get(i).getFriendID().FriendID1).getId())
                    .vorname(userRepository.findUserById(anderelist.get(i).getFriendID().FriendID1).getVorname())
                    .nachname(userRepository.findUserById(anderelist.get(i).getFriendID().FriendID1).getNachname())
                    .build();
        }
        return anderearr;
    }

    public void deleteFriend(String jwtToken, Long FriendId){
        friendRepository.delete(friendRepository.secondRequest(getUserFromToken(jwtToken).getId(), FriendId));
    }
}
