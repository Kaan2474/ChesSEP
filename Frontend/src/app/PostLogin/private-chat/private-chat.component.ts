import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatService} from "../../Service/chat.service";
import {Chat} from "../../Modules/Chat";

@Component({
  selector: 'app-private-chat',
  templateUrl: './private-chat.component.html',
  styleUrls: ['./private-chat.component.css']
})
export class PrivateChatComponent implements OnInit,OnDestroy {
  token = localStorage.getItem("JWT");
  id: any;
  messages: Chat[] = [];
  newMessage: any = {}; // Initialisiere newMessage als leeres Objekt
  user: any;
  friend: any;
  chat: any;
  chatid:any;
  content:any;
  senderName:any;


  constructor(private userService: UserService,
              private chatService: ChatService,
              private route: ActivatedRoute,
              private router: Router) {

    this.user = new User();
    this.friend = new User();
    this.chat = new Chat();
           }

  ngOnInit() {
    this.getUserDetail();
    this.getFriendDetails();
    this.getChatId();

  }
  ngOnDestroy(): void {
  }
  getUserDetail() {
    this.userService.getUserbyToken().subscribe((data) => {
      this.user = data;
      console.log('User details:', this.user); // Füge diese Zeile hinzu

    },);
  }
  getFriendDetails(){
    this.id = this.route.snapshot.params["id"];
    this.userService.getUser(this.id).subscribe((data) => {
      this.friend = data;
      console.log('Friend details:', this.friend); // Füge diese Zeile hinzu
    },);
  }
  getChatId(){
    this.chatService.getMyPrivateChatWith(this.id).subscribe(data =>{
      this.chat=data;
      this.chatid=data.chatId;
      console.log('Chat details:', this.chat)
      this.loadChatMessages();
    })
  }

  loadChatMessages() {
     this.chatService.getChatMessages(this.chatid).subscribe((data) => {
       this.messages = data;
       for (let i = 0; i < data.length; i++) {
         this.userService.getUser(data[i].senderId).subscribe( res=>
           this.messages[i].senderName = res.vorname)
       }
       console.log('Loaded messages:', this.messages); // Füge diese Zeile hinzu
     });
  }
  sendMessage(content:String) {
    if (content === undefined || content === "") {
      alert("Sie können keine leere Nachricht senden!");
    } else {
      this.newMessage = {
        senderId: this.user.id,
        recipientId: this.friend.id,
        content: this.content
      };
      this.chatService.writeMessage(this.chatid, this.newMessage).subscribe(() => {
         this.loadChatMessages();
         this.content = "";
       });
    }
          }
}
