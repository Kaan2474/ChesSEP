import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {ChessClubService} from "../../Service/chess-club.service";
import {ChessClub} from "../../Modules/ChessClub";
import {User} from "../../Modules/User";
import {ActivatedRoute, Router} from "@angular/router";
import {Chat} from "../../Modules/Chat";
import {ChatService} from "../../Service/chat.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-schachclub',
  templateUrl: './schachclub.component.html',
  styleUrls: ['./schachclub.component.css']
})
export class SchachclubComponent implements OnInit{

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
  sub:Subscription = new Subscription;
  lastMessageTime:bigint=BigInt(0);


  constructor(
    private chessclubservice: ChessClubService,
    private route: ActivatedRoute,
    private chatService: ChatService,
    private userService: UserService,
    private router: Router
  ) {
    this.user= new User();
    this.chessClubId = this.route.snapshot.params["id"];
  }

  ngOnInit() {
    this.getAllMemberList();
      this.getGroupChatId();
      this.getMyClubName();
    console.log(this.chessClubId)
    this.userService.getUserbyToken().subscribe(data=>
      this.user = data
    )
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
      this.loadChatMessages();
    })
  }
  loadChatMessages() {
    this.chatService.getChatMessages(this.groupchatId,this.lastMessageTime).subscribe((data) => {
      this.messages = data;
      for (let i = 0; i < data.length; i++) {
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
      this.chatService.writeMessageGroup(this.groupchatId, this.newMessage).subscribe(() => {
        this.loadChatMessages();
        this.content = "";
      });
    }
  }
  deleteGroupMessage(message:Chat){
    this.chatService.deleteMessage(this.groupchatId,message).subscribe(()=> {
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
      this.chatService.changeMessage(this.groupchatId, message).subscribe(() => {
        message.editable = false;
        this.loadChatMessages();
      });
    }
  }
  leaveChessClub(){
    this.chessclubservice.leaveChessClub(this.chessClubId).subscribe(()=>
      this.router.navigate(['/homepage']))
  }
}



