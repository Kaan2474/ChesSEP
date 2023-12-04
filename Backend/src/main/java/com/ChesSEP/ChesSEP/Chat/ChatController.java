package com.ChesSEP.ChesSEP.Chat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
