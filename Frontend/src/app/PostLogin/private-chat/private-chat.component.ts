import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatService} from "../../Service/chat.service";
import {Chat} from "../../Modules/Chat";
import {interval, Subscription} from "rxjs";

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
  private refreshTimer:Subscription;
  private changeableMessages: any;
  lastMessageTime:bigint=BigInt(0);
  changedMessages:any[]=[];

  constructor(private userService: UserService,
              private chatService: ChatService,
              private route: ActivatedRoute,
              private router: Router) {

    this.user = new User();
    this.friend = new User();
    this.chat = new Chat();
    this.refreshTimer = new Subscription();
  }

  ngOnInit() {
    this.getUserDetail();
    this.getFriendDetails();
    this.getChatId();
    this.refreshChat();

  }
  ngOnDestroy(): void {
    this.refreshTimer.unsubscribe();
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
      this.loadChatMessages(BigInt(0));
    })
  }
  checkForMessageChange():boolean{
    console.log("suchenachContent")
    this.chatService.getChangeableMessages(this.chatid).subscribe(messages=>{

      if(this.changedMessages.length!=messages.length){
        this.loadChatMessages(BigInt(0));
        this.changedMessages=messages;
        return true;
      }
      this.changedMessages=messages;

      for (let i = 0; i < this.messages.length; i++) {
        for (let j = 0; j < messages.length; j++) {
          if(this.messages[i].messageId!=messages[j].messageId)
            continue;

          if(this.messages[i].content!=messages[j].content){
            console.log("ungelicher Content gefunden")
            this.loadChatMessages(BigInt(0));
            return true;
          }
        }
      }
      return false;
    })
    return false;
  }

  loadChatMessages(lastMessageTime:bigint) {
    this.chatService.getChatMessages(this.chatid,lastMessageTime).subscribe((data) => {
      if(data.length==0)
        return;
      this.messages = data;
      this.lastMessageTime=this.messages[this.messages.length-1].time;

      for (let i = 0; i < data.length; i++) {
        this.userService.getUser(data[i].senderId).subscribe(res =>
          this.messages[i].senderName = res.vorname)
      }
      console.log('Loaded messages:', this.messages);
      this.chatService.getChangeableMessages(this.chatid).subscribe(data=>{
        this.changeableMessages = data;
        console.log("Neue Nachrichten: " + this.changeableMessages)
      });
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
        this.loadChatMessages(this.lastMessageTime);
         this.content = "";
       });
    }
  }
  deleteMessage(message:Chat){
    this.chatService.deleteMessage(this.chatid,message).subscribe(()=> {
      this.loadChatMessages(this.lastMessageTime);
      window.location.reload();
    });
  }
  edit(message: Chat) {
    message.editable = true;
    message.newContent = message.content;
  }

  sendEdit(message:Chat){
    if (message.editable) {
      this.chatService.changeMessage(this.chatid, message).subscribe(() => {
        message.editable = false;
        this.loadChatMessages(this.lastMessageTime);
      });
    }
  }
  refreshChat(){
    this.refreshTimer = interval(1000).subscribe(()=>{
      this.checkForMessageChange()
      this.loadChatMessages(this.lastMessageTime);
    })
  }
}
