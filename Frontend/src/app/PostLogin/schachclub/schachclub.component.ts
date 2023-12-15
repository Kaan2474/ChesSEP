import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {ChessClubService} from "../../Service/chess-club.service";
import {ChessClub} from "../../Modules/ChessClub";
import {User} from "../../Modules/User";
import {ActivatedRoute, Router} from "@angular/router";
import {Chat} from "../../Modules/Chat";
import {ChatService} from "../../Service/chat.service";
import {interval, Subscription} from "rxjs";

@Component({
  selector: 'app-schachclub',
  templateUrl: './schachclub.component.html',
  styleUrls: ['./schachclub.component.css']
})
export class SchachclubComponent implements OnInit,OnDestroy{

  token = localStorage.getItem("JWT");
  chessClubMembers: User[] = [];
  chessClubId: any;
  content : any;
  user: User;
  newMessage: any = {};
  id:any[]=[];
  messages: Chat[] = [];
  chessClubName: any;
  groupchatId:any;
  private refreshTimer:Subscription;
  private changeableMessages: any;
  lastMessageTime:bigint=BigInt(0);
  changedMessages:any[]=[];


  constructor(
    private chessclubservice: ChessClubService,
    private route: ActivatedRoute,
    private chatService: ChatService,
    private userService: UserService,
    private router: Router
  ) {
    this.user= new User();
    this.chessClubId = this.route.snapshot.params["id"];
    this.refreshTimer = new Subscription();
  }

  ngOnInit() {
    this.getAllMemberList();
      this.getGroupChatId();
      this.getMyClubName();
    console.log(this.chessClubId)
    this.userService.getUserbyToken().subscribe(data=>
      this.user = data
    )
    this.refreshChat();

  }
  ngOnDestroy(){
    this.refreshTimer.unsubscribe();
  }

  getAllMemberList() {
    this.chessclubservice.getMembers(this.chessClubId).subscribe(data => {
        this.chessClubMembers = data;

      }, error => {
        console.error("Fehler beim Laden der Mitglieder", error);
      }
    );
  }

  getMyClubName(){
    this.chessclubservice.getMyChessClubname().subscribe(data=>{
      this.chessClubName = data
      console.log(this.chessClubName)})

  }
  getGroupChatId(){
    this.chessclubservice.getChessClubById(this.chessClubId).subscribe(data =>{
      this.groupchatId=data.chatId;
      this.loadChatMessages(BigInt(0));
    })
  }
  loadChatMessages(lastMessageTime:bigint) {
    this.chatService.getChatMessages(this.groupchatId,lastMessageTime).subscribe((data) => {
      if(data.length==0)
        return;
      this.messages = data;
      this.lastMessageTime=this.messages[this.messages.length-1].time;
      for (let i = 0; i < data.length; i++) {
        this.userService.getUser(data[i].senderId).subscribe(res =>
          this.messages[i].senderName = res.vorname)
      }
      console.log('Loaded messages:', this.messages);
      this.chatService.getChangeableMessages(this.groupchatId).subscribe(data=>{
        this.changeableMessages = data;
        console.log("Neue Nachrichten: " + this.changeableMessages)
      });
    });
  }
  checkForMessageChange():boolean{
    console.log("suchenachContent")
    this.chatService.getChangeableMessages(this.groupchatId).subscribe(messages=>{

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

  sendMessage(content:String) {
    if (content === undefined || content === "") {
      alert("Sie können keine leere Nachricht senden!");
    } else {
      this.newMessage = {
        senderId: this.user.id,
        user: this.id,
        content: content
      };
      this.chatService.writeMessageGroup(this.groupchatId, this.newMessage).subscribe(() => {
        this.loadChatMessages(this.lastMessageTime);
        this.content = "";
      });
    }
  }
  deleteGroupMessage(message:Chat){
    this.chatService.deleteMessage(this.groupchatId,message).subscribe(()=> {
      this.loadChatMessages(this.lastMessageTime);
      window.location.reload();
    });
  }
  edit(message: Chat) {
    message.editable = true;
  }

  sendEdit(message:Chat){
    if (message.editable) {
      this.chatService.changeMessage(this.groupchatId, message).subscribe(() => {
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
  leaveChessClub(){
    this.chessclubservice.leaveChessClub(this.chessClubId).subscribe(()=>
      this.router.navigate(['/homepage']))
  }
}



