import {Component, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute} from "@angular/router";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {Chess} from "../../Modules/Chess";


@Component({
  selector: 'app-play-game-against-user',
  templateUrl: './play-game-against-user.component.html',
  styleUrls: ['./play-game-against-user.component.css']
})
export class PlayGameAgainstUserComponent implements OnInit {
  id:any;
  user: any;
  gegner:any
  token = localStorage.getItem("JWT");
  url = "assets/images/profil-picture-icon.png"
  chessGame : any;

  constructor(private userService: UserService,private route: ActivatedRoute, private matchmakinService: MatchmakingService) {
    this.user = new User();
    this.gegner = new User();
    this.chessGame = new Chess();
  }
  ngOnInit() {
    this.getMyCurrentMatch();
    this.getUserDetail();
    this.id = this.route.snapshot.params["id"];
  }

  getUserDetail() {
    this.userService.getUserbyToken().subscribe((data) => {
        console.log(data)
        this.user = data

        if(this.user.profilbild!=null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
      },
      error => {
        console.error("Fehler beim Laden der Benutzerdaten");
      });
  }
  getProfileGegner(gegnerID:any){
    this.userService.getUser(gegnerID).subscribe(data=> {
        this.gegner = data;
        console.log(this.gegner)
        if(this.gegner.profilbild!=null) {
          this.gegner.profilbild = 'data:image/png;base64,' + this.gegner.profilbild;
        }
      },
      error => {
        console.error('Error getting user profile:', error);
      }
    );
  }

  getMyCurrentMatch(){
    this.matchmakinService.getMyCurrentMatch().subscribe(data =>{
      this.chessGame = data;
        if(this.user.id === this.chessGame.playerWhiteID){
          this.id = this.chessGame.playerBlackID;
        }else {
          this.id = this.chessGame.playerWhiteID;
        }
        this.getProfileGegner(this.id);
      console.log('friendID:' + this.id);
      console.log('friendID:' + this.chessGame.playerBlackID);
    })
  }
}
