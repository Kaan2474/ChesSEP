import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {Chess} from "../../Modules/Chess";
import { Friends } from 'src/app/Modules/Friends';
import { Subscription, interval } from 'rxjs';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-play-game-against-user',
  templateUrl: './play-game-against-user.component.html',
  styleUrls: ['./play-game-against-user.component.css']
})
export class PlayGameAgainstUserComponent implements OnInit,OnDestroy {
  id:any;
  user: User=new User();
  gegner:User=new User();
  token = localStorage.getItem("JWT");
  url = "assets/images/profil-picture-icon.png"
  chessGame : any= new Chess();

  constructor(private userService: UserService,
    private route: ActivatedRoute, 
    private matchmakinService: MatchmakingService,
    private http: HttpClient,
    private router: Router) 
    {}
   
  sub:Subscription=new Subscription;

  ngOnInit() {
    this.getMyCurrentMatch();
    this.getUserDetail();
    this.getProfileGegner();

    console.log('user:' + this.user.id);
    console.log('gegner:' + this.gegner.id);
    
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
    this.userService.getUserbyToken().subscribe((data) => {
        this.user = data;
        console.log('user:' + this.user.id);

        if(this.user.profilbild!=null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
      },
      error => {
        console.error("Fehler beim Laden der Benutzerdaten");
      });
  }

  getProfileGegner(){

    this.matchmakinService.getMyCurrentEnemy().subscribe(data=>{
      this.gegner.id=data;
      console.log('gegner:' + this.gegner.id);
      this.getGegner();
    })
  }

  getGegner(){
    this.userService.getUser(this.gegner.id).subscribe(data=> {
      this.gegner = data;
      console.log(this.gegner)

      if(this.gegner.profilbild!=null) {
        this.gegner.profilbild = 'data:image/png;base64,' + this.gegner.profilbild;
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
