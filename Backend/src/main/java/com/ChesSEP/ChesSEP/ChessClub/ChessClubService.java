package com.ChesSEP.ChesSEP.ChessClub;


import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChessClubService {

    private final UserRepository userRepository;
    private final ChessClubRepository chessClubRepository;


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

        user.setClubId(null);
        userRepository.save(user);
    }

    public void joinClub(String clubName){
        User user = userRepository.findUserById(getSender().getId());

        user.setClubId(chessClubRepository.findChessClubByName(clubName).getId());
        userRepository.save(user);
    }

    public void createClub(String clubName){
        chessClubRepository.save(ChessClub.builder()
                .name(clubName)
                .build());

        joinClub(clubName);
    }

    public List<ChessClub> getAllChessClubs(){
        return chessClubRepository.getAllChessClubs();
    }
}
