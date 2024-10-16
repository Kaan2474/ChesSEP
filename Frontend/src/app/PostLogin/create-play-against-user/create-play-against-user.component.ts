import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {UserService} from "../../Service/user.service";
import {interval, Observable, Subscription} from "rxjs";
import {Chess} from "../../Modules/Chess";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-create-play-against-user',
  templateUrl: './create-play-against-user.component.html',
  styleUrls: ['./create-play-against-user.component.css']
})
export class CreatePlayAgainstUserComponent implements OnInit{
  public allFriends: Friends[] = [];
  URL = "http://localhost:8080/match";

  token = localStorage.getItem("JWT");

  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

  chessGame:any; //soll einfach die JSON file in der Console anzeigen {"gameid", "match-length", "name", "blackid", "whiteid", "timeStamp"}
  allChessGames:any;
  user: any;
  constructor(private http: HttpClient,
  private friendsService: FriendsService,
  private matchmakingservice:MatchmakingService,
  private userService: UserService,
  private router: Router)

  {

  }


  ngOnInit() {

    var waited=localStorage.getItem("Waited");
    if(waited=="1"){
      this.matchmakingservice.cancelMatchRequest().subscribe();
      this.matchmakingservice.dequeueMatch().subscribe();

      localStorage.setItem("Waited","0");
    }
    this.userService.getUserbyToken().subscribe(data=>
    this.user = data);
    this.getFriendsList()
    this.matchmakingservice.getallMatches().subscribe(data=>{
      this.allChessGames=data;
    })

  }


  getFriendsList() {
    this.friendsService.getFriendslist()
      .subscribe((data) => {
        console.log(data)
        this.allFriends = data;
      });
  }

  sendRequestMatch(user: Friends){


    this.http.post(this.URL + "/requestMatch",user, {headers: this.header})
      .subscribe(data => {
        this.waitForMatch(this.chessGame)
        console.log(data)
      });
    this.showNotification("Die Einladung wurde erfolgreich versendet")
  }

  queueForMatch() {
    this.matchmakingservice.queueMatch().subscribe(data => {
      console.log(data)
      this.waitForMatch(this.chessGame)
    })
  }

  waitForMatch(chess: Chess) {
    this.router.navigate(["/waiting"]);
  }

  showNotification(message:string){
    alert(message);
  }
  refresh(){
  window.location.reload();
  }
  setGameID(gameID:any, playerID:any, rivalID:any){
    localStorage.setItem("GameID", gameID);
    localStorage.setItem("PlayerID", playerID);
    localStorage.setItem("GegnerplayerID", rivalID);
    this.router.navigate(["/stream/" + gameID]);
  }
}




