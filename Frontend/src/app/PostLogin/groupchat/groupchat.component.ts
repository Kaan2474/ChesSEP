import {Component, OnDestroy, OnInit} from '@angular/core';
import {Chat} from "../../Modules/Chat";
import {UserService} from "../../Service/user.service";
import {ChatService} from "../../Service/chat.service";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../Modules/User";
import {interval, Subscription, switchMap} from "rxjs";


@Component({
  selector: 'app-groupchat',
  templateUrl: './groupchat.component.html',
  styleUrls: ['./groupchat.component.css']
})
export class GroupchatComponent implements OnInit, OnDestroy{

  token = localStorage.getItem("JWT");
  groupName=this.route.snapshot.params["id"];
  id:any[]=[];
  messages: Chat[] = [];
  newMessage: any = {}; // Initialisiere newMessage als leeres Objekt
  user: any;
  groupChat: any;
  groupId:any;
  content:any;
  membersIds:any[]=[];
  members:User[]=[];
  private refreshTimer:Subscription;
  private changeableMessages: any;

  constructor(private userService: UserService,
              private chatService: ChatService,
              private route: ActivatedRoute,
              private router: Router,) {

    this.user = new User();
    this.groupChat = new Chat();
    this.refreshTimer = new Subscription();
  }
  ngOnInit() {
    this.getUserDetail();
    this.getGroupChatId();
    console.log(this.groupName);
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
  getGroupChatId(){
    this.chatService.getGroupByGroupName(this.groupName).subscribe(data =>{
      this.groupChat=data;
      this.groupId=data.chatId;
      console.log('Chat details:', this.groupChat);
      this.loadChatMessages();
      this.getMemberList();
    })
  }
  loadChatMessages() {
    this.chatService.getChatMessages(this.groupId).subscribe((data) => {

        if(data.length<=this.messages.length){
          return;
        }
        this.messages = data;
        for (let i = 0; i < data.length; i++) {
            this.userService.getUser(data[i].senderId).subscribe(res =>
                this.messages[i].senderName = res.vorname)
        }
        console.log('Loaded messages:', this.messages); // Füge diese Zeile hinzu
      this.chatService.getChangeableMessages(this.groupId).subscribe(data=>{
        this.changeableMessages = data;
        console.log("Neue Nachrichten: " + this.changeableMessages)
      })
    });
  }
    getMemberList(){
        this.chatService.membersOfGroupChat(this.groupId).subscribe(res=>{
            this.membersIds=res;
            for (let i= 0;  i<res.length ; i++) {
              if(this.membersIds[i]===null){

              }
                this.userService.getUser(this.membersIds[i]).subscribe(res2=>
                    this.members[i]=res2)
            }
            console.log(this.members)
        })
    }

  sendMessage(content:String) {
    if (content === undefined || content === "") {
      alert("Sie können keine leere Nachricht senden!");
    } else {
      this.newMessage = {
        senderId: this.user.id,
        user: this.id,
        content: this.content
      };
      this.chatService.writeMessageGroup(this.groupId, this.newMessage).subscribe(() => {
        this.loadChatMessages();
        this.content = "";
      });
    }
  }
  deleteGroupMessage(message:Chat){
    this.chatService.deleteMessage(this.groupId,message).subscribe(()=> {
      this.loadChatMessages();
      window.location.reload();
    });
  }
  edit(message: Chat) {
    message.editable = true;
    message.newContent = message.content;
  }

  sendEdit(message:Chat){
    if (message.editable) {
      this.chatService.changeMessage(this.groupId, message).subscribe(() => {
        message.editable = false;
        this.loadChatMessages();
      });
    }
  }
  refreshChat(){
    this.refreshTimer = interval(1000).subscribe(()=>{
      this.loadChatMessages();
    })
  }
}
