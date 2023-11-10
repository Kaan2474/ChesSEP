package com.ChesSEP.ChesSEP.Friendlist;

import com.ChesSEP.ChesSEP.Email.EmailService;
import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.Privacy;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;


    private User getUserFromToken(String jwtToken){
        return userRepository.findByEmail(tokenService.extractEmail(jwtToken.substring(7)));
    }

    public void sendFriendRequest(String jwtToken, String friendemail){
        long friendId=userRepository.findByEmail(friendemail).getId();
        User sender=getUserFromToken(jwtToken);

        if(friendRepository.isFriend(sender.getId(), friendId) != null ||
        friendRepository.searchRequest(sender.getId(), friendId) != null)
        return;

        if(friendRepository.getRequest(sender.getId(), friendId) != null){
            Friend request=friendRepository.getRequest(sender.getId(), friendId);
            request.setType(FriendTyp.FRIEND);
            friendRepository.save(request);
            return;
        }

        friendRepository.save(Friend.builder()
            .friendID(new FriendID(sender.getId(), friendId))
            .type(FriendTyp.REQUEST)
            .build());

        emailService.send(getUserFromToken(jwtToken).getId(), friendId,
            "ChesSEP - Freundschaftsanfrage",
            getUserFromToken(jwtToken).getVorname() + " " + getUserFromToken(jwtToken).getNachname() + " möchte mit dir befreundet sein.");     
    }

    public void acceptFriendRequest(String jwtToken, Long friendId){
        User sender=getUserFromToken(jwtToken);

        Friend request=friendRepository.getRequest(sender.getId(), friendId);

        if(request == null || friendRepository.isFriend(sender.getId(), friendId) != null)
        return;

        request.setType(FriendTyp.FRIEND);

        friendRepository.save(request);
    }

    public void cancelFriendRequest(String jwtToken, Long friendId){
        friendRepository.delete(friendRepository.searchRequest(getUserFromToken(jwtToken).getId(), friendId));
    }

    public void denyFriendRequest(String jwtToken, Long friendId){
        friendRepository.delete(friendRepository.searchRequest(friendId, getUserFromToken(jwtToken).getId()));
    }
    
    public UserRequestHolder[] getMyFriendlist (String jwtToken){
        Long userId=getUserFromToken(jwtToken).getId();
        List<Friend> list = friendRepository.getFriendlist(userId);

        UserRequestHolder[] arr = new UserRequestHolder[list.size()];

        for (int i = 0; i < arr.length; i++) {
            FriendID currentObj=list.get(i).getFriendID();
            User currentFriend;

            if(userId==currentObj.FriendID1){
                currentFriend=userRepository.findUserById(currentObj.FriendID2);
            }else{
                currentFriend=userRepository.findUserById(currentObj.FriendID1);
            }

            arr[i] = UserRequestHolder.builder()
                    .id(currentFriend.getId())
                    .vorname(currentFriend.getVorname())
                    .nachname(currentFriend.getNachname())
                    .build();
        }
        return arr;
    }

    public UserRequestHolder[] getFriendlistOf (String jwtToken, UserRequestHolder target){
        if(userRepository.findUserById(target.getId()).getFriendlistPrivacy() == Privacy.PRIVAT){
                  return null;
        }
        Long userId=target.getId();
        List<Friend> list = friendRepository.getFriendlist(userId);

        UserRequestHolder[] arr = new UserRequestHolder[list.size()];

        for (int i = 0; i < arr.length; i++) {
            FriendID currentObj=list.get(i).getFriendID();
            User currentFriend;

            if(userId==currentObj.FriendID1){
                currentFriend=userRepository.findUserById(currentObj.FriendID2);
            }else{
                currentFriend=userRepository.findUserById(currentObj.FriendID1);
            }

            arr[i] = UserRequestHolder.builder()
                    .id(currentFriend.getId())
                    .vorname(currentFriend.getVorname())
                    .nachname(currentFriend.getNachname())
                    .build();
        }
        return arr;
    }

    public void deleteFriend(String jwtToken, Long friendId){
        friendRepository.delete(friendRepository.getRequest(getUserFromToken(jwtToken).getId(), friendId));
    }
}
