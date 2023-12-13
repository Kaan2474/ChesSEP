import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {ChessClubService} from "../../Service/chess-club.service";
import {ChessClub} from "../../Modules/ChessClub";
import {User} from "../../Modules/User";
import {ActivatedRoute} from "@angular/router";
import {Chat} from "../../Modules/Chat";
import {ChatService} from "../../Service/chat.service";

@Component({
  selector: 'app-schachclub',
  templateUrl: './schachclub.component.html',
  styleUrls: ['./schachclub.component.css']
})
export class SchachclubComponent implements OnInit{

  chessClubMembers: User[] = [];
  chessClubId: any;
  groupName: any;
  content : any;
  user: User;
  newMessage: any = {};
  groupChat: any;
  id:any[]=[];
  messages: Chat[] = [];
  membersIds:any[]=[];
  chessClubName: any = "testtt222";



  constructor(
    private chessclubservice: ChessClubService,
    private route: ActivatedRoute,
    private chatservice: ChatService,
    private userService: UserService
  ) {
    this.user= new User();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.chessClubId = +params['id'];
    this.getAllMemberList()
      this.getGroupChatId()
      this.getMyClubName()
    });
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
    this.chatservice.getChesClubChatByName(this.chessClubName).subscribe(data =>{
      this.groupChat=data;
      this.chessClubId=data.chatId;
      console.log('Chat details:', this.groupChat);
      this.loadChatMessages();
      this.getAllMemberList()
      this.chatservice.membersOfGroupChat(this.chessClubId).subscribe(res=>{
        this.membersIds=res;
        for (let i= 0;  i<res.length ; i++) {

        }
      })
    })
  }

  writeMessage(content: String){
    if (content === undefined || content === "") {
      alert("Sie können keine leere Nachricht senden!");
    } else {
      this.newMessage = {
        senderId: this.user.id,
        user: this.id,
        content: this.content
      };
      this.chatservice.writeMessageGroup(this.chessClubId, this.newMessage).subscribe(() => {
        this.loadChatMessages();
        this.content = "";
      });
    }
  }


  loadChatMessages() {
    this.chatservice.getChatMessages(this.chessClubId).subscribe((data) => {
      this.messages = data;
      for (let i = 0; i < data.length; i++) {
        this.userService.getUser(data[i].senderId).subscribe(res =>
          this.messages[i].senderName = res.vorname)
      }
      console.log('Loaded messages:', this.messages);
    });
  }
}



