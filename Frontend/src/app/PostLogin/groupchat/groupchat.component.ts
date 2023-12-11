import { Component } from '@angular/core';
import {Chat} from "../../Modules/Chat";
import {UserService} from "../../Service/user.service";
import {ChatService} from "../../Service/chat.service";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../Modules/User";

@Component({
  selector: 'app-groupchat',
  templateUrl: './groupchat.component.html',
  styleUrls: ['./groupchat.component.css']
})
export class GroupchatComponent {

  token = localStorage.getItem("JWT");
  groupName=this.route.snapshot.params["id"];
  id:any[]=[];
  messages: Chat[] = [];
  newMessage: any = {}; // Initialisiere newMessage als leeres Objekt
  user: any;
  friends: User[]= [];
  groupChat: any;
  groupId:any;
  content:any;
  membersIds:any[]=[];
  members:any[]=[];

  constructor(private userService: UserService,
              private chatService: ChatService,
              private route: ActivatedRoute,
              private router: Router) {

    this.user = new User();
    this.groupChat = new Chat();
  }
  ngOnInit() {
    this.getUserDetail();
    this.getGroupChatId();
    console.log(this.groupName);

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
    console.log('Alle Mitglieder:', this.groupChat.user); // Füge diese Zeile hinzu
      for (let i = 0; i < this.groupChat.user.length; i++) {
          this.userService.getUser(this.groupChat.user[i]).subscribe(data => {
              if(this.groupChat.user[i]!==this.user.id){
                  this.friends[i] = data;
              }
          });
      }
      console.log('Alle Mitglieder bis auf man selbst:', this.friends); // Füge diese Zeile hinzu
  }
  getGroupChatId(){
    this.chatService.getGroupByGroupName(this.groupName).subscribe(data =>{
      this.groupChat=data;
      this.groupId=data.chatId;
      console.log('Chat details:', this.groupChat);
      this.loadChatMessages();
      this.getFriendDetails();
      this.chatService.membersOfGroupChat(this.groupId).subscribe(res=>{
        this.membersIds=res;
        for (let i= 0;  i<res.length ; i++) {
          //this.userService.getUser(this.membersIds[i]).subscribe(res2=>
            //this.members[i]=res2)
        }
      })
    })
  }
  loadChatMessages() {
    this.chatService.getChatMessages(this.groupId).subscribe((data) => {
        this.messages = data;
        for (let i = 0; i < data.length; i++) {
            this.messages[i].messageId = {
                senderId: data[i].senderId,
                chatId: data[i].chatId,
                time: data[i].time
            }
            this.userService.getUser(data[i].senderId).subscribe(res =>
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
        user: this.id,
        content: this.content
      };
      this.chatService.writeMessageGroup(this.groupId, this.newMessage).subscribe(() => {
        this.loadChatMessages();
        this.content = "";
      });
    }
  }
}
