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

    private User getSender() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    ChatService(ChatMessageRepository chatMessageRepository, ChatRepository chatRepository, ChessClubRepository chessClubRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRepository = chatRepository;
        this.chessClubRepository = chessClubRepository;
        this.userRepository = userRepository;
    }



    //###################Alles Rund um Private Unterhaltungen#######################
    public boolean createPrivateChat(Long friendId) {

        if (chatRepository.getPrivateChat(getSender().getId(), friendId) != null) {
            return false;
        } else {

            chatRepository.save(Chat.builder()
                    .ownerId(getSender().getId())
                    .recipientId(friendId)
                    .type(ChatType.PRIVATE)
                    .build());
            return true;
        }
    }


    //###############Alles Rund um Gruppen Unterhaltungen###################

    //Gruppenchat Erstellung
    public boolean createGroupChat(List<Long> user, String privateGroupName) {

        if (user.isEmpty()|| chatRepository.findChatByGroupName(privateGroupName) != null) {
            return false;
        }
        user.add(getSender().getId());
        chatRepository.save(Chat.builder()
                .ownerId(getSender().getId())
                .user(user)
                .privateGroupName(privateGroupName)
                .type(ChatType.GROUP)
                .build());
        return true;
    }

    //Für das Erstellen eines ChessClubChats, welcher beim erstellen eines Clubs direkt mit erstellt wird
    public void createChessClubChat(String name) {

        List<Long> member = new ArrayList<>();
        member.add(getSender().getId());

        chatRepository.save(Chat.builder()
                .ownerId(getSender().getId())
                .user(member)
                .chessClubName(name)
                .type(ChatType.CLUB)
                .build());

        ChessClub a = chessClubRepository.findChessClubByName(name);
        a.setChatId(chatRepository.findChatByClubName(name).getChatId());
        chessClubRepository.save(a);
    }


    //Liste der ChatTeilnehmer wird um getSender erweitert

    public void updateChessClubChat(String clubName) {
        Chat a = chatRepository.findChatByClubName(clubName);
        List<User> member = userRepository.getChessClubMember(chessClubRepository.findChessClubByName(clubName).getId());
        List<Long> updatedList = new ArrayList<>();
        for (User x : member) {
            updatedList.add(x.getId());
        }
        a.setUser(updatedList);
        chatRepository.save(a);

    }


    //Gibt ein Chat Objekt zurück, welches mit dem privateGroupName übereinstimmt
    public Chat getGroupByGroupName(String privateGroupName){
        return chatRepository.findChatByGroupName(privateGroupName);
    }


    // Hinzufügen von Teilnehmer in Gruppenchats --> ChessClub Bedingung fehlt
    public boolean addMemberToGroupChat(long chatId, long newMemberId) {
        Chat chat = chatRepository.findChatByChatId(chatId);
        if (chat.getUser().contains(newMemberId)) {
            return false;
        } else {
            chat.getUser().add(newMemberId);
            chatRepository.save(chat);
            return true;
        }
    }

    //Gibt private Chat Objekt aus
    public Chat getPrivateChatWith(long friendId){
        return chatRepository.getPrivateChat(getSender().getId(), friendId);
    }

    //Rückgabe von Member aus einem Gruppenchat
    public List<Long> memberOfChatId(long chatId) {
        List<Long> members;
        members = chatRepository.findChatByChatId(chatId).getUser();
        return members;
    }

    //Gitb alle Gruppenchats zurück, in denen ich Mitglied bin
    public List<Chat> findAllMyGroupChats(){
        return chatRepository.findAllGroupChatsOfUserId(getSender().getId());
    }




    //##################Alles Rund um Nachrichten###############################


    //Für das schreiben von Nachrichten in Privaten Unterhaltungen (1 zu 1)
    //findByChatIDAndUserId -> Buggy
    public boolean writeMessage(String content, long chatId) {
        if (content == null || chatRepository.findChatByChatId(chatId) == null){ // || chatRepository.findByChatIdAndUserId(chatId, getSender().getId()) == null) {
            return false;
        } else {
            chatMessageRepository.save(ChatMessage.builder()
                    .messageId(new MessageId(getSender().getId(), chatId, System.currentTimeMillis()))
                    .chatMessageStatus(ChatMessageStatus.UNREAD)
                    .content(content)
                    .build());
            return true;
        }
    }


    //Für das schreiben von Nachrichten in Gruppen
    public boolean writeMessageGroup(String content, long chatId) {
        if (content == null || chatRepository.findChatByChatId(chatId) == null || chatRepository.findByChatIdAndUserId(chatId, getSender().getId()) == null) {
            return false;
        } else {
            chatMessageRepository.save(ChatMessage.builder()
                    .messageId(new MessageId(getSender().getId(), chatId, System.currentTimeMillis()))
                    .chatMessageStatus(ChatMessageStatus.UNREAD)
                    .content(content)
                    .build());
            return true;
        }
    }


    //Liste aller Nachrichten, nach newestMessageTime
    //Sobald jemand anderes die Nachricht abruft, wird der Status der Nachricht auf ChatMessageStatus.READ gesetzt
    public List<ChatMessage> getNewMessage(long chatId, long newestMessageTime) {

        List<ChatMessage> list = chatMessageRepository.findNewMessageOf(chatId, newestMessageTime);
        for (ChatMessage x : list) {
            if (x.messageId.senderId != getSender().getId()) {
                x.setChatMessageStatus(ChatMessageStatus.READ);
                chatMessageRepository.save(x);
            }
        }
        return list;
    }



    //Lösche Chat aus DB -> Für Owner
    public void deleteChat(String clubName) {
        if (getSender().getId().equals(chatRepository.findChatByGroupName(clubName).getOwnerId())) {
            chatRepository.delete(chatRepository.findChatByClubName(clubName));
        }
    }



    //Gibt Nachrichten aus chatId aus
    public List<ChatMessage> findChatMessagesOf(long chatId) {
        return chatMessageRepository.findChatMessagesOf(chatId);
    }


    //Gibt all meine Nachrichten aus, die ich grschrieben haben
    private List<ChatMessage> myLastWrittenMessage(long chatId, long senderId) {
        return chatMessageRepository.myWrittenMessage(chatId, senderId);
    }

    //Gibt eine Liste aus, mit den Nachrichten, die auf UNREAD stehen
    public List<ChatMessage> myChangeableMessage(long chatId) {
        List<ChatMessage> messages = myLastWrittenMessage(chatId, getSender().getId());
        List<ChatMessage> unreadMessage = new ArrayList<>();
        if (messages.isEmpty()) {
            return null;
        }
        for (ChatMessage x : messages) {
            if (x.getChatMessageStatus() == ChatMessageStatus.UNREAD) {
                unreadMessage.add(x);
            }
        }
        return unreadMessage;
    }


    //Verändert eine Nachricht, die ich geschrieben habe
    public boolean setChangeMessage(long chatId, String oldContent, String newContent) {
        if(chatRepository.findByChatIdAndUserId(chatId, getSender().getId())==null){
            return false;
        }

        List<ChatMessage> list = myChangeableMessage(chatId);
        for (ChatMessage x : list) {
            if (x.getContent().equals(oldContent)) {
                x.setContent(newContent);
                chatMessageRepository.save(x);
                return true;
            }
        }
        return false;
    }

    //Löscht eine von mir geschriebene Nachricht
    public boolean deleteMessage(long chatId, String oldContent){
        if(chatRepository.findByChatIdAndUserId(chatId, getSender().getId())==null){
            return false;
        }
        List<ChatMessage> list = myChangeableMessage(chatId);
        for(ChatMessage x : list){
            if(x.getContent().equals(oldContent)){
                chatMessageRepository.delete(x);
                return true;
            }
        }
        return false;
    }





    ////////////////////////Methoden die eig nicht mehr gebraucht werden/////////////////////////////////////////////////////////////////////////


    public boolean leaveGroupChat(String privateGroupName) {
        if (chatRepository.findChatByGroupName(privateGroupName) != null && chatRepository.findChatByGroupName(privateGroupName).getUser().contains(getSender().getId())) {
            Chat leftGroup = chatRepository.findChatByGroupName(privateGroupName);
            leftGroup.getUser().remove(getSender().getId());
            chatRepository.save(leftGroup);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteGroupChat(String privateGroupName) {
        Chat toDelete = chatRepository.findChatByGroupName(privateGroupName);
        if (toDelete != null && Objects.equals(toDelete.getOwnerId(), getSender().getId())) {
            chatRepository.delete(toDelete);
            return true;
        } else {
            return false;
        }
    }

    public boolean deletePrivateChat(long recipientId) {
        Chat toDelete = chatRepository.getPrivateChat(getSender().getId(), recipientId);
        if (toDelete != null) {
            chatRepository.delete(toDelete);
            return true;
        } else {
            return false;
        }
    }
}
    /*
        ##Gibt alle Nachrichten aus, die vor Time geschrieben wurden##

    public List<ChatMessage> getAllMessage(long chatId, long lastMessageTime){
        ChatMessage chat = chatMessageRepository.findLatestMessage(chatId);
        if(chat == null){
            return null;
        }

        if(lastMessageTime>= chat.getMessageId().time)
            return  null;

        return findChatMessagesOf(chatId);
    }
 */


    /*
    Muss nicht konventiert werden, List übergibt das selbe Format. getestet in Postman

    public ChatMessage[] chatMessageListToArray(List<ChatMessage> messages){
        if(messages == null){
            return null;
        }

        ChatMessage [] array = new ChatMessage[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            array[i] = messages.get(i);
        }
        return array;
    }
     */

