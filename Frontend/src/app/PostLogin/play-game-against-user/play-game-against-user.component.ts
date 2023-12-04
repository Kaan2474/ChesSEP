import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {Chess} from "../../Modules/Chess";
import { Subscription, interval } from 'rxjs';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-play-game-against-user',
  templateUrl: './play-game-against-user.component.html',
  styleUrls: ['./play-game-against-user.component.css']
})
export class PlayGameAgainstUserComponent implements OnInit,OnDestroy {
  id:any;
  user: User;
  rival: User;
  token = localStorage.getItem("JWT");
  url = "assets/images/profil-picture-icon.png";
  chessGame : any= new Chess();

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private matchmakinService: MatchmakingService,
    private http: HttpClient,
    private router: Router) {
      this.user = new User();
      this.rival = new User();
    }

  sub:Subscription = new Subscription;

  ngOnInit() {
    this.getMyCurrentMatch();
    this.getUserDetail();
    this.getIdOfRival();

    this.id = this.route.snapshot.params["id"];

    this.refreshMatch();
  }

  ngOnDestroy(){
    this.sub.unsubscribe();
  }



  refreshMatch() {
    this.sub = interval(250).subscribe(data => {
      this.matchmakinService.getMyCurrentMatch().subscribe(chess => {
        if(chess==null){
          this.router.navigate(["/homepage"]);
        }
      });
    });
  }

  getUserDetail() {
    this.userService.getUserbyToken().subscribe(data => {
        this.user = data;
        if(this.user.profilbild != null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
      },
      error => {
        console.error("Fehler beim Laden der Benutzerdaten");
      });
  }

  //Gibt die ID des Gegners zurück
  getIdOfRival(){
    this.matchmakinService.getMyCurrentEnemy().subscribe(data=> {
      this.rival.id = data;
      this.getRival();
    })
  }

  //Speichert den Gegner in der Variable rival anhand der ID
  getRival(){
    this.userService.getUser(this.rival.id).subscribe(data=> {
      this.rival = data;
      if(this.rival.profilbild!=null) {
        this.rival.profilbild = 'data:image/png;base64,' + this.rival.profilbild;
      }
    }
  );
  }

  getMyCurrentMatch(){
    this.matchmakinService.getMyCurrentMatch().subscribe(data =>{
      this.chessGame = data;
    })
  }

  endGame(){
    this.matchmakinService.endMyMatch().subscribe();
  }
}
