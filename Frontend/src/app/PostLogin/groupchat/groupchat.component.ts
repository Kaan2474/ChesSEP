import {Component, OnInit} from '@angular/core';
import {Chat} from "../../Modules/Chat";
import {UserService} from "../../Service/user.service";
import {ChatService} from "../../Service/chat.service";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../Modules/User";
import {interval, Subscription} from "rxjs";


@Component({
  selector: 'app-groupchat',
  templateUrl: './groupchat.component.html',
  styleUrls: ['./groupchat.component.css']
})
export class GroupchatComponent implements OnInit{

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
  sub:Subscription = new Subscription;

  constructor(private userService: UserService,
              private chatService: ChatService,
              private route: ActivatedRoute,
              private router: Router,) {

    this.user = new User();
    this.groupChat = new Chat();
  }
  ngOnInit() {
    this.getUserDetail();
    this.getGroupChatId();
    console.log(this.groupName);
  }
  ngOnDestroy(): void {
    this.sub.unsubscribe();
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
        this.messages = data;
        for (let i = 0; i < data.length; i++) {
            this.userService.getUser(data[i].senderId).subscribe(res =>
                this.messages[i].senderName = res.vorname)
        }
        console.log('Loaded messages:', this.messages); // Füge diese Zeile hinzu
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
  deleteGroupMessage(message:Chat, i :any){
    this.chatService.deleteMessage(this.groupId,message).subscribe(()=> {
      this.loadChatMessages();
      window.location.reload();
    });
  }
  edit(message: Chat, index: number) {
    message.editable = true;
    message.newContent = message.content;
  }

  checkEditable(i:any):boolean{
    if(this.messages[i].chatMessageStatus==='UNREAD'){
      return true;
    }
    return false;
  }

  sendEdit(message:Chat){
    if (message.editable) {
      this.chatService.changeMessage(this.groupId, message).subscribe(() => {
        message.editable = false;
        this.loadChatMessages();
      });
    }
  }
}
