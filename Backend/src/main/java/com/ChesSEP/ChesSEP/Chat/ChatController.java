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

    @PostMapping("/createPrivateChat")
    public ResponseEntity<String> createPrivateChat(@RequestBody ChatRequestDto friend) {
        boolean check = chatService.createPrivateChat(friend.getRecipientId());
        if (!check) {
            return new ResponseEntity<>("Gruppe gibt es schon", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Erfolgreich", HttpStatus.CREATED);
        }
    }

    @PostMapping("/createGroupChat")
    public ResponseEntity<String> createGroupChat(@RequestBody ChatRequestDto user){

        List<Long> member = user.getUser();
        boolean check = chatService.createGroupChat(member, user.getGroupName());
        if(check) {
            return new ResponseEntity<>("Erfolgreich erstellt", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Gruppe darf nicht leer sein", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/leaveGroup")
    public ResponseEntity<String> leaveGroupChat(@RequestBody ChatRequestDto user) {
        String check = chatService.leaveGroupChat(user.getGroupName());
        if (check.equals("Du hast den Gruppenchat erfolgreich verlassen")) {
            return new ResponseEntity<>(check, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteGroupChat/{privateGroupName}")
    public ResponseEntity<String> deleteGroupChat(@PathVariable String privateGroupName){
        String check = chatService.deleteGroupChat(privateGroupName);
        if(check.equals("Erfolgreich gelöscht")){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deletePrivateChat")
    public ResponseEntity<String> deletePrivateChat(@RequestBody ChatRequestDto friend){
        String check = chatService.deletePrivateChat(friend.getRecipientId());
        if(check.equals("Erfolgreich gelöscht")){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/addMember/{chatId}")
    public ResponseEntity<String> addMember(@PathVariable Long chatId, @RequestBody ChatRequestDto newMember){
        String check = chatService.addMemberToGroupChat(chatId, newMember.getRecipientId());
        if(check.equals("User erfolgreich hinzugefügt")){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    //privateChat Message
    @PostMapping("/writeMessagePrivateChat/{chatId}")
    public ResponseEntity<Boolean> writeMessage(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto){
        Boolean check = chatService.writeMessage(chatRequestDto.getContent(), chatId);
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/writeMessageGroup/{chatId}")
    public ResponseEntity<Boolean> writeMessageGroup(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto) {
        Boolean check = chatService.writeMessageGroup(chatRequestDto.getContent(), chatId);
        if (check) {
            return new ResponseEntity<>(check, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    //Gibt alle Members aus einer Gruppe zurück - klappt - für GruppenChat
    @GetMapping("/members/{chatId}/{groupName}")
    public ResponseEntity<List<Long>> membersOfChatId(@PathVariable long chatId, @PathVariable String groupName){
        return ResponseEntity.ok(chatService.memberOfChatId(chatId,groupName));
    }

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

    //Verändert eine Ungelesene Nachricht
    @PostMapping("/changeMessage/{chatId}")
    public ResponseEntity<Boolean> changeMessage(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto){
        boolean check = chatService.setChangeMessage(chatId, chatRequestDto.getOldContent(), chatRequestDto.getNewContent());
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    //löscht eine ungelesene Nachricht
    @PostMapping("/deleteMessage/{chatId}")
    public ResponseEntity<Boolean> deleteMessage(@PathVariable long chatId, @RequestBody ChatRequestDto chatRequestDto){
        boolean check = chatService.deleteMessage(chatId, chatRequestDto.getOldContent());
        if(check){
            return new ResponseEntity<>(check, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        }
    }

    //Findet alle private und gruppen chats
    @GetMapping("/allMyChats")
    public List<Chat> findAllMyPrivateChats(){ return chatService.findAllMyChats();}


    /*
    ###########Deprecated############
    @GetMapping("/getMessages/{chatId}")
    public ResponseEntity<List<ChatMessage>> messages (@PathVariable long chatId){
        return ResponseEntity.ok(chatService.findChatMessagesOf(chatId));
    }
     */

}
