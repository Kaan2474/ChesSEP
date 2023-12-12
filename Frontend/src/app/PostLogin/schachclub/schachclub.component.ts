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
  schachClubname: any;
  content : any;
  user: User;
  newMessage: any = {};
  selectedChatId: any;
  changeMessages: Chat[] = [];
  newMessageContent: string = "";



  constructor(
    private chessclubservice: ChessClubService,
    private route: ActivatedRoute,
    private chatservice: ChatService
  ) {
    this.user= new User();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.chessClubId = +params['id'];
    this.getAllMemberList()
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
      this.schachClubname = data
      console.log(this.schachClubname)})

  }

  writeMessage(){


  }

}
