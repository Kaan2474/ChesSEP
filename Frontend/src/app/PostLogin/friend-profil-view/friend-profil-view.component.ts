import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {User} from "../../Modules/User";
import {ActivatedRoute} from "@angular/router";
import {ChessClubService} from "../../Service/chess-club.service";
import {ChessClub} from "../../Modules/ChessClub";

@Component({
  selector: 'app-friend-profil-view',
  templateUrl: './friend-profil-view.component.html',
  styleUrls: ['./friend-profil-view.component.css']
})
export class FriendProfilViewComponent implements OnInit {

  id: any;
  user: User;
  token = localStorage.getItem("JWT");
  chessClub: any ;

  constructor(private userService: UserService,
              private route: ActivatedRoute,
              private chessclubservice: ChessClubService) {
              this.user = new User()



  }
  ngOnInit() {
    this.id = this.route.snapshot.params["id"];
    console.log('userId:', this.id);
    this.getProfileFriend();


}
  getProfileFriend(){
    this.userService.getUser(this.id).subscribe(data=> {
      this.user = data;
      console.log(this.user)
      if(this.user.profilbild!=null) {
        this.user.profilbild = 'data:image/png;base64,' + this.user.profilbild;
      }
      if(this.user.compleatedPuzzles >= 3){
        this.user.achievement = "Schachexperte";
      }else{
        this.user.achievement= "kein Achievement";
      }
      this.getChessClubOf();
      },
    );
  }
  getChessClubOf(){
    this.chessclubservice.getChessClubOf(this.id).subscribe(date =>{
      if(date === null){
        this.chessClub =this.user.vorname + " gehört keinem Club an";
      }else {
        this.chessClub = date;
        console.log(this.chessClub)
      }})

  }
}



