package com.ChesSEP.ChesSEP.Chat;

import com.ChesSEP.ChesSEP.ChessClub.ChessClub;
import com.ChesSEP.ChesSEP.ChessClub.ChessClubRepository;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ChatService {


    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;

    private final ChessClubRepository chessClubRepository;

    private final UserRepository userRepository;

    private User getSender(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    ChatService(ChatMessageRepository chatMessageRepository, ChatRepository chatRepository, ChessClubRepository chessClubRepository,UserRepository userRepository){
        this.chatMessageRepository = chatMessageRepository;
        this.chatRepository = chatRepository;
        this.chessClubRepository = chessClubRepository;
        this.userRepository = userRepository;
    }

    /*UserRequestHolder mit Übergeben und dann dort die Id als ownerId setzen, ChatType wird gesetzt, jenachdem wie viele im Chat sind
    wird nur eien Person eingeladen = privat
    ab > 1 = Group
     */
    public boolean createPrivateChat(Long friendId){

        if(chatRepository.getPrivateChat(getSender().getId(), friendId) != null) {
            return false;
        }else {

            chatRepository.save(Chat.builder()
                    .ownerId(getSender().getId())
                    .recipientId(friendId)
                    .type(ChatType.PRIVATE)
                    .build());
            /*Chat chat = Chat.builder()
                    .ownerId(getSender().getId())
                    .recipientId(friendId)
                    .type(ChatType.PRIVATE)
                    .build();
            chatRepository.save(chat);

             */
            return true;
        }
    }

    public boolean createGroupChat(List<Long> user, String groupName){

        if(user.isEmpty()){
            return false;
        }
        user.add(getSender().getId());
        chatRepository.save(Chat.builder()
                .ownerId(getSender().getId())
                .user(user)
                .privateGroupName(groupName)
                .type(ChatType.GROUP)
                .build());
        return true;
    }

    public void createChessClubChat(String name){

        List<Long> member = new ArrayList<>();
        member.add(getSender().getId());

        chatRepository.save(Chat.builder()
                .ownerId(getSender().getId())
                .chessClubId(chessClubRepository.findChessClubByName(name).getId())
                .user(member)
                .chessClubName(name)
                .type(ChatType.CLUB)
                .build());

        ChessClub a = chessClubRepository.findChessClubByName(name);
        a.setChatId(chatRepository.findChatByClubName(name).getChatId());
        chessClubRepository.save(a);
    }


    /*
    Liste der Teilnehmer, die Nachrichten bekommen, wird um getSender erweitert
     */
    public void updateChessClubChat(String clubName){
        Chat a = chatRepository.findChatByClubName(clubName);
        List<User> member = userRepository.getChessClubMember(chessClubRepository.findChessClubByName(clubName).getId());
        List<Long> updatedList = new ArrayList<>();
        for(User x : member){
            updatedList.add(x.getId());
        }
        a.setUser(updatedList);
        chatRepository.save(a);

    }

    public String leaveGroupChat(String groupName){
        if(chatRepository.findChatByGroupName(groupName)!= null && chatRepository.findChatByGroupName(groupName).getUser().contains(getSender().getId())){
            Chat leftGroup = chatRepository.findChatByGroupName(groupName);
            leftGroup.getUser().remove(getSender().getId());
            chatRepository.save(leftGroup);
            return "Du hast den Gruppenchat erfolgreich verlassen";
        }else{
            return "Entweder existiert die Gruppe nicht oder du bist gar kein Mitglied dieser Gruppe";
        }
    }

    public String deleteGroupChat(String privateGroupName){
        Chat toDelete = chatRepository.findChatByGroupName(privateGroupName);
        if(toDelete != null && Objects.equals(toDelete.getOwnerId(), getSender().getId())){
            chatRepository.delete(toDelete);
            return "Erfolgreich gelöscht";
        }else{
            return "Etwas ist fehlgeschlagen";
        }
    }

    public String deletePrivateChat(Long recipientId){
        Chat toDelete = chatRepository.getPrivateChat(getSender().getId(), recipientId);
        if(toDelete!=null){
            chatRepository.delete(toDelete);
            return "Erfolgreich gelöscht";
        }else{
            return "Etwas ist fehlgeschlagen";
        }
    }

    public String addMemberToGroupChat(Long chatId, Long newMemberId){
        Chat chat = chatRepository.findChatByChatId(chatId);
        if(chat.getUser().contains(newMemberId)){
            return "User existiert bereits";
        }else{
            chat.getUser().add(newMemberId);
            chatRepository.save(chat);
            return "User erfolgreich hinzugefügt";
        }
    }

    public void deleteChat(String clubName){
        chatRepository.delete(chatRepository.findChatByClubName(clubName));
    }

    public List<ChatMessage> findChatMessagesOf(Chat chatid){
        return chatMessageRepository.findChatMessagesOf(chatid);
    }

}
