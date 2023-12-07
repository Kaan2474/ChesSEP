package com.ChesSEP.ChesSEP.ChessClub;


import com.ChesSEP.ChesSEP.Chat.ChatService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import com.ChesSEP.ChesSEP.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChessClubService {

    private final UserRepository userRepository;
    private final ChessClubRepository chessClubRepository;
    private final ChatService chatService;
    private final UserService userService;



    private User getSender(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public UserRequestHolder[] getMembers(String clubName){
        Long chessClubId = chessClubRepository.findChessClubByName(clubName).getId();
        List<User> list = userRepository.getChessClubMember(chessClubId);

        UserRequestHolder[] arr = new UserRequestHolder[list.size()];

        for (int i = 0; i < arr.length; i++) {
            User currentUser=list.get(i);
            arr[i] = UserRequestHolder.builder()
                    .id(currentUser.getId())
                    .vorname(currentUser.getVorname())
                    .nachname(currentUser.getNachname())
                    .elo(currentUser.getElo())
                    .build();
        }
        return arr;
    }

    public void leaveClub(){
        User user = userRepository.findUserById(getSender().getId());
        ChessClub chessClub = chessClubRepository.findChessClubById(user.getClubId());

        user.setClubId(null);
        userRepository.save(user);

        if(userRepository.getChessClubMember(chessClub.getId()) == null){
            chessClubRepository.delete(chessClub);
        }
    }

    public void joinClub(String clubName){
        User user = userRepository.findUserById(getSender().getId());
        ChessClub chessClub = chessClubRepository.findChessClubById(user.getClubId());

        user.setClubId(chessClubRepository.findChessClubByName(clubName).getId());
        userRepository.save(user);

        if(userRepository.getChessClubMember(chessClub.getId()) == null){
            chessClubRepository.delete(chessClub);
        }
    }

    public void createClub(String clubName){
        if(chessClubRepository.findChessClubByName(clubName)!=null){
            return;
        }

        chessClubRepository.save(ChessClub.builder()
                .name(clubName)
                .build());

        joinClub(clubName);
    }

    public String getMeinChessClubName(){
        User user = userRepository.findUserById(getSender().getId());
        return chessClubRepository.findChessClubById(user.getClubId()).getName();
    }

    public ChessClub[] getAllChessClubs(){
        List<ChessClub> list = chessClubRepository.getAllChessClubs();

        ChessClub[] arr = new ChessClub[list.size()];

        for (int i = 0; i < arr.length; i++) {
            ChessClub chessClub=list.get(i);
            arr[i] = ChessClub.builder()
                    .id(chessClub.getId())
                    .name(chessClub.getName())
                    .build();
        }
        return arr;
    }




    /*
    Beim joinen wird clubId vom getSender überschrieben und er wird in die Chat Liste eingetragen
     */
    public void joinClubByMario(String clubname) {
        User newMember = getSender();
        if (getSender().getClubId() == null) {
            newMember.setClubId(chessClubRepository.findChessClubByName(clubname).getId());
            userRepository.save(newMember);
            chatService.updateChessClubChat(clubname);
        } else {
            newMember.setClubId(chessClubRepository.findChessClubByName(clubname).getId());
            deleteClubV2(clubname);
        }
    }

    public boolean createClubV2(String clubName){
        User user = userRepository.findUserById(getSender().getId());
        if(chessClubRepository.findChessClubByName(clubName)!=null){
            return false;
        }else if(user.getClubId() != null){
            return false;
        }else {

            chessClubRepository.save(ChessClub.builder()
                    .name(clubName)
                    .build());

            user.setClubId(chessClubRepository.findChessClubByName(clubName).getId());
            userRepository.save(user);


            chatService.createChessClubChat(clubName);
            joinClubByMario(clubName);
            return true;
        }
    }


    public void deleteClubV2(String clubName){
        List<User> member = userRepository.getChessClubMember(chessClubRepository.findChessClubByName(clubName).getId());
        if(member.isEmpty()){
            chessClubRepository.delete(chessClubRepository.findChessClubByName(clubName));

        }
    }

    public UserRequestHolder[] getChessClubMember(long chessId){
        List<User> list = userRepository.getChessClubMember(chessId);
        UserRequestHolder[] x = new UserRequestHolder[list.size()];

        for (int i = 0; i < list.size() ; i++) {
            x[i]  = userService.convetToRequestHolder(list.get(i));
        }
        return x;
    }


}
