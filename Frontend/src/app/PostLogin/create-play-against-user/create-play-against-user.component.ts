import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {interval, Observable, Subscription} from "rxjs";
import {Chess} from "../../Modules/Chess";

@Component({
  selector: 'app-create-play-against-user',
  templateUrl: './create-play-against-user.component.html',
  styleUrls: ['./create-play-against-user.component.css']
})
export class CreatePlayAgainstUserComponent implements OnInit, OnDestroy{
  public allFriends: Friends[] = [];
  URL = "http://localhost:8080/match";

  token = localStorage.getItem("JWT");

  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

  chessGame:any; //soll einfach die JSON file in der Console anzeigen {"gameid", "match-length", "name", "blackid", "whiteid", "timeStamp"}

  sub : Subscription = new Subscription();

  constructor(private http: HttpClient,
  private friendsService: FriendsService,
  private matchmakingservice:MatchmakingService)

  {

  }



  ngOnInit() {
    this.getFriendsList()
  }

  ngOnDestroy(){
    this.sub.unsubscribe();
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
        console.log(data)
        this.myMatchRequest()
      });
  }


  myMatchRequest(){

    this.http.get(this.URL + "/getMyMatchRequest", {headers: this.header})
      .subscribe(data => {
        console.log(data)
      });
  }


  queueForMatch(){
    this.matchmakingservice.queueMatch().subscribe(data => {
      console.log(data)
      this.intervalMatchRequest(this.chessGame)

    })
  }

  intervalMatchRequest(chess:Chess) {
    this.sub = interval(250).subscribe(data => {
      this.http.get(this.URL + "/getMyCurrentMatch", {headers: this.header}).subscribe(chess => {
        this.chessGame = chess;
        console.log(this.chessGame.timeStamp)
        if (this.chessGame != null) {
          this.ngOnDestroy()
          console.log(this.chessGame.gameID)

        }
      });
    });
  }
}




