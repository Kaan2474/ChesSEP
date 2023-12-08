import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {ChessClubService} from "../../Service/chess-club.service";
import {ChessClub} from "../../Modules/ChessClub";
import {User} from "../../Modules/User";

@Component({
  selector: 'app-schachclub',
  templateUrl: './schachclub.component.html',
  styleUrls: ['./schachclub.component.css']
})
export class SchachclubComponent implements OnInit{

  allMemberList: User[] = [];

  constructor(
    private chessclubservice: ChessClubService
  ) {
  }

  ngOnInit() {
    this.getAllMemberList()
  }

  getAllMemberList(){
    this.chessclubservice.getMembers().subscribe(data=>
      this.allMemberList = data)

  }


}
