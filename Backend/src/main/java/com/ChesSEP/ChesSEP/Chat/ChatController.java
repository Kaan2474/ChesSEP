package com.ChesSEP.ChesSEP.Chat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    //####################Alles rund um Private Unterhaltung################################

    //Erstelle eine Private Uneterhaltung
    @GetMapping("/createPrivateChat/{friendId}")
    public ResponseEntity<Boolean> createPrivateChat(@PathVariable long friendId) {
        boolean check = chatService.createPrivateChat(friendId);
        if (!check) {
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(check, HttpStatus.CREATED);
        }
    }

    /*Schreibe eine Message an die Unterhaltung ->chatId (Message an eine Private Unterhaltung)
    Der Content wird aus dem Objekt ChatRequestDto entnommen -> String content
     */
    @PostMapping("/writeMessagePrivateChat/{chatId}")
    public ResponseEntity<Boolean> writeMessage(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto){
        Boolean check = chatService.writeMessage(chatRequestDto.getContent(), chatId);
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    //Gibt einen Privaten Chat zurück, wo SenderId umd EmpfängerId übereinstimmen (auch umgekehrt)
    @GetMapping("/getMyPrivateChatWith/{friendId}")
    public ResponseEntity<Chat> getMyPrivateChatWith(@PathVariable long friendId){
        Chat chat = chatService.getPrivateChatWith(friendId);
        if(chat != null){
            return new ResponseEntity<>(chat, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(chat, HttpStatus.BAD_REQUEST); //wenn Chat == null
        }
    }

    //####################Alles rund um Gruppen Unterhaltung################################

    //Erstelle eine Gruppen Uneterhaltung mit mehreren Usern
    @GetMapping("/createGroupChat/{groupName}/{user}")
    public ResponseEntity<Boolean> createGroupChat(@PathVariable String groupName, @PathVariable List<Long> user){

        boolean check = chatService.createGroupChat(user, groupName);
        if(check) {
            return new ResponseEntity<>(check, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    /*Schreibe eine Message an die Gruppen Unterhaltung mit der angegebenen chatId
    Der Content wird aus dem Objekt ChatRequestDto entnommen -> String content
     */
    @PostMapping("/writeMessageGroup/{chatId}")
    public ResponseEntity<Boolean> writeMessageGroup(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto) {
        Boolean check = chatService.writeMessageGroup(chatRequestDto.getContent(), chatId);
        if (check) {
            return new ResponseEntity<>(check, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    //Gibt alle Members aus einer Gruppe Unterhaltung zurück
    @GetMapping("/members/{chatId}")
    public ResponseEntity<List<Long>> membersOfChatId(@PathVariable long chatId){
        return ResponseEntity.ok(chatService.memberOfChatId(chatId));
    }


    //Gibt den Chat zurück, der mit der Gruppenbezeichnung übereinstimmt
    @GetMapping("/getGroupByGroupName/{privateGroupName}")
    public ResponseEntity<Chat> getGroupByGroupName(@PathVariable String privateGroupName){
        Chat chat = chatService.getGroupByGroupName(privateGroupName);
        if(chat!=null){
            return new ResponseEntity<>(chat, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(chat, HttpStatus.BAD_REQUEST);
        }
    }

    //Gibt alle Gruppen Unterhaltungen zurück, die dem Sender zugeordnet werden können
    @GetMapping("/allMyGroupChats")
    public List<Chat> findAllMyGroupChats(){ return chatService.findAllMyGroupChats();}




    //#################Alles Rund um Nachrichten#######################


    //Gibt neuste Nachricht(en) zurück
    @GetMapping("/latest/{chatId}/{time}")
    public ResponseEntity<List<ChatMessage>> findLatestMessage(@PathVariable long chatId, @PathVariable long time){
        return ResponseEntity.ok(chatService.getNewMessage(chatId, time));
    }


    //Gibt eine List aus, mit allen Nachrichten die ungelesen sind und somit verändert/gelöscht werden können
    @GetMapping("/changableMessage/{chatId}")
    public ResponseEntity<List<ChatMessage>> lastWrittenChangeableMessage(@PathVariable long chatId){
        return ResponseEntity.ok(chatService.myChangeableMessage(chatId));
    }



    /*Verändert den Content einer UNGELESENEN "UNREAD" Nachricht
    Hier ist zu verstehen, dass getOldContent verglichen wird mit der Nachricht, die in der DB zu finden ist.
    Bei einer übereinstimmung, wird diese zu getNewContent verändert
     */
    @PostMapping("/changeMessage/{chatId}")
    public ResponseEntity<Boolean> changeMessage(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto){
        boolean check = chatService.setChangeMessage(chatId, chatRequestDto.getOldContent(), chatRequestDto.getNewContent());
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }


    /*Löscht eine UNGELESENEN "UNREAD" Nachricht
    Hier ist zu verstehen, dass getOldContent verglichen wird mit der Nachricht, die in der DB zu finden ist.
    Bei einer übereinstimmung, wird diese gelöscht
     */
    @PostMapping("/deleteMessage/{chatId}")
    public ResponseEntity<Boolean> deleteMessage(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto){
        boolean check = chatService.deleteMessage(chatId, chatRequestDto.getOldContent());
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    //Gibt die Nachrichten aus, die in einer Unterhaltung (Gruppe oder Private) sind
    @GetMapping("/getMessages/{chatId}")
    public ResponseEntity<List<ChatMessage>> messages (@PathVariable long chatId){
        return ResponseEntity.ok(chatService.findChatMessagesOf(chatId));
    }





    ///////////////Verworfene Methoden//////////////////////////////////
    @PostMapping("/leaveGroup")
    public ResponseEntity<Boolean> leaveGroupChat(@RequestBody ChatRequestDto user) {
        boolean check = chatService.leaveGroupChat(user.getPrivateGroupName());
        if (check) {
            return new ResponseEntity<>(check, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteGroupChat/{privateGroupName}")
    public ResponseEntity<Boolean> deleteGroupChat(@PathVariable String privateGroupName){
        boolean check = chatService.deleteGroupChat(privateGroupName);
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deletePrivateChat")
    public ResponseEntity<Boolean> deletePrivateChat(@RequestBody ChatRequestDto friend){
        boolean check = chatService.deletePrivateChat(friend.getRecipientId());
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/addMember/{chatId}")
    public ResponseEntity<Boolean> addMember(@PathVariable Long chatId, @RequestBody ChatRequestDto newMember){
        boolean check = chatService.addMemberToGroupChat(chatId, newMember.getRecipientId());
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

}
