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
    public ResponseEntity<Boolean> createPrivateChat(@RequestBody ChatRequestDto friend) {
        boolean check = chatService.createPrivateChat(friend.getRecipientId());
        if (!check) {
            return new ResponseEntity<>(check, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(check, HttpStatus.CREATED);
        }
    }

    @PostMapping("/createGroupChat")
    public ResponseEntity<Boolean> createGroupChat(@RequestBody ChatRequestDto chatRequestDto){

        List<Long> member = chatRequestDto.getUser();
        boolean check = chatService.createGroupChat(member, chatRequestDto.getGroupName());
        if(check) {
            return new ResponseEntity<>(check, HttpStatus.CREATED);
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
    @GetMapping("/members/{chatId}")
    public ResponseEntity<List<Long>> membersOfChatId(@PathVariable long chatId){
        return ResponseEntity.ok(chatService.memberOfChatId(chatId));
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


    @GetMapping("/getMessages/{chatId}")
    public ResponseEntity<List<ChatMessage>> messages (@PathVariable long chatId){
        return ResponseEntity.ok(chatService.findChatMessagesOf(chatId));
    }


    ///////////////Verworfene Methoden//////////////////////////////////
    @PostMapping("/leaveGroup")
    public ResponseEntity<Boolean> leaveGroupChat(@RequestBody ChatRequestDto user) {
        boolean check = chatService.leaveGroupChat(user.getGroupName());
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
