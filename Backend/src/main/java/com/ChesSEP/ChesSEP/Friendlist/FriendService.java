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

    public boolean sendFriendRequest(String jwtToken, String friendemail){
        long friendId=userRepository.findByEmail(friendemail).getId();
        User sender=getUserFromToken(jwtToken);
        if(sender.getId() == friendId)
            return false;

        if(friendRepository.getFriends(sender.getId(), friendId) != null ||
        friendRepository.getRequest(sender.getId(), friendId) != null)
        return false;

        if(friendRepository.getRequest(sender.getId(), friendId) != null){
            Friend request=friendRepository.getRequest(sender.getId(), friendId);
            request.setType(FriendTyp.FRIEND);
            friendRepository.save(request);
            return true;
        }

        friendRepository.save(Friend.builder()
            .friendID(new FriendID(sender.getId(), friendId))
            .type(FriendTyp.REQUEST)
            .build());

        emailService.send(getUserFromToken(jwtToken).getId(), friendId,
            "ChesSEP - Freundschaftsanfrage",
            getUserFromToken(jwtToken).getVorname() + " " + getUserFromToken(jwtToken).getNachname() + " möchte mit dir befreundet sein.");
        return true;
    }

    public void acceptFriendRequest(String jwtToken, Long friendId){
        User sender=getUserFromToken(jwtToken);

        if(sender.getId() == friendId) return;

        Friend request=friendRepository.getRequest(sender.getId(), friendId);

        if(request == null || friendRepository.getFriends(sender.getId(), friendId) != null)
        return;

        request.setType(FriendTyp.FRIEND);

        friendRepository.save(request);
    }

    public void denyFriendRequest(String jwtToken, Long friendId){
        Long id=getUserFromToken(jwtToken).getId();
        Friend request=friendRepository.getRequest(friendId, id);
        friendRepository.delete(request);
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
                    .email(currentFriend.getEmail())
                    .nachname(currentFriend.getNachname())
                    .elo(currentFriend.getElo())
                    .geburtsdatum(currentFriend.getGeburtsdatum())
                    .build();
        }
        return arr;
    }

    public UserRequestHolder[] getFriendlistOf (String jwtToken, Long id){
                
        User user=userRepository.findUserById(id);

        if(user.getFriendlistPrivacy() == Privacy.PRIVAT){
            return null;
        }
        
        List<Friend> list = friendRepository.getFriendlist(user.getId());

        UserRequestHolder[] arr = new UserRequestHolder[list.size()];

        for (int i = 0; i < arr.length; i++) {
            FriendID currentObj=list.get(i).getFriendID();
            User currentFriend;

            if(user.getId()==currentObj.FriendID1){
                currentFriend=userRepository.findUserById(currentObj.FriendID2);
            }else{
                currentFriend=userRepository.findUserById(currentObj.FriendID1);
            }

            arr[i] = UserRequestHolder.builder()
                    .id(currentFriend.getId())
                    .vorname(currentFriend.getVorname())
                    .nachname(currentFriend.getNachname())
                    .elo(currentFriend.getElo())
                    .geburtsdatum(currentFriend.getGeburtsdatum())
                    .build();
        }
        return arr;
    }

    public UserRequestHolder[] getMyPendingFriendRequests(String jwtToken){
        User user=getUserFromToken(jwtToken);
        List<Friend> requests=friendRepository.getFriendRequests(user.getId());

        UserRequestHolder[] result=new UserRequestHolder[requests.size()];

        for (int i = 0; i < result.length; i++) {

            User currentUser;
            FriendID currentRequestIDs=requests.get(i).getFriendID();

            if(currentRequestIDs.FriendID1==user.getId()){
                currentUser=userRepository.findUserById(currentRequestIDs.FriendID2);
            }else{
                currentUser=userRepository.findUserById(currentRequestIDs.FriendID1);
            }

            result[i]=UserRequestHolder.builder()
                .id(currentUser.getId())
                .vorname(currentUser.getVorname())
                .nachname(currentUser.getNachname())
                .build();

        }

        return result;
    }

    public void deleteFriend(String jwtToken, Long friendId){
        friendRepository.delete(friendRepository.getFriends(getUserFromToken(jwtToken).getId(),friendId));
    }
}
