import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {ChessClubService} from "../../Service/chess-club.service";
import {ChessClub} from "../../Modules/ChessClub";
import {User} from "../../Modules/User";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-schachclub',
  templateUrl: './schachclub.component.html',
  styleUrls: ['./schachclub.component.css']
})
export class SchachclubComponent implements OnInit{

  chessClubMembers: User[] = [];
  chessClubId: any;


  constructor(
    private chessclubservice: ChessClubService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.chessClubId = +params['id'];
    this.getAllMemberList()
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

}
