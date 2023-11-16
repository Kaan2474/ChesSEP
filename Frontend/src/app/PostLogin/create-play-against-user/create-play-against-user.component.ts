import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {User} from "../../Modules/User";
import {interval, Subscription} from "rxjs";

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

  data: any; //soll einfach die JSON file in der Console anzeigen {"gameid", "match-length", "name", "blackid", "whiteid", "timeStamp"}
  interval = interval(250)

  constructor(private http: HttpClient,
  private friendsService: FriendsService,
  private matchmakingservice:MatchmakingService) { }


  ngOnInit() {
    this.getFriendsList()
  }

  ngOnDestroy(){
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
      this.intervalMatchRequest()
      console.log(data)
    })
  }

  intervalMatchRequest(){
    this.interval.subscribe( data => {
      this.http.get(this.URL + "/getMyCurrentMatchID", {headers: this.header})
    })
 }



}



