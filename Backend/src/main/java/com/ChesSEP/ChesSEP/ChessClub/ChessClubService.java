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

    public boolean createClubV2(String clubName){
        if(chessClubRepository.findChessClubByName(clubName)!=null){
            return false;
        }else {

            chessClubRepository.save(ChessClub.builder()
                    .name(clubName)
                    .build());

            getSender().setClubId(chessClubRepository.findChessClubByName(clubName).getId());
            userRepository.save(getSender());


            chatService.createChessClubChat(clubName);
            joinClub(clubName);
            return true;
        }
    }

    public void joinClub(String clubName) {
        if (getSender().getClubId() == null) {
            getSender().setClubId(chessClubRepository.findChessClubByName(clubName).getId());
            userRepository.save(getSender());
            chatService.updateChessClubChat(clubName);
        } else {
            getSender().setClubId(chessClubRepository.findChessClubByName(clubName).getId());
            userRepository.save(getSender());
            deleteClubV2(clubName);
        }
    }

    public void leaveClub(){
        User user = userRepository.findUserById(getSender().getId());
        ChessClub chessClub = chessClubRepository.findChessClubById(user.getClubId());

        if(user.getClubId() == null){
            return;
        }

        user.setClubId(null);
        userRepository.save(user);
        deleteClubV2(chessClub.getName());
    }

    public String getMeinChessClubName(){

        if(getSender().getClubId()==null){
            return "du bist in keinem Club";
        }

        return chessClubRepository.findChessClubById(getSender().getClubId()).getName();
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

    public UserRequestHolder[] getChessClubMember(long chessId){
        List<User> list = userRepository.getChessClubMember(chessId);
        UserRequestHolder[] x = new UserRequestHolder[list.size()];

        for (int i = 0; i < list.size() ; i++) {
            x[i]  = userService.convetToRequestHolder(list.get(i));
        }
        return x;
    }

    public void deleteClubV2(String clubName){
        List<User> member = userRepository.getChessClubMember(chessClubRepository.findChessClubByName(clubName).getId());
        if(member.isEmpty()){
            chessClubRepository.delete(chessClubRepository.findChessClubByName(clubName));

        }
    }
}
