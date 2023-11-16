import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {User} from "../../Modules/User";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";

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

  constructor(private http: HttpClient,
  private friendsService: FriendsService,
  private matchmakingservice:MatchmakingService,
              private router: Router) { }

  ngOnInit() {
    this.getFriendsList()
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
        console.log(data);
        this.myMatchRequest()
      });
    this.showNotification("Einladung wurde erfolgreich versendet")
  }


  myMatchRequest(){

    this.http.get<any>(this.URL + "/getMyMatchRequest", {headers: this.header})
      .subscribe(data => {
        console.log(data)

          this.router.navigate(["/play-game-against-user"]).then(()=> { this.myMatchRequest();
        });
      });
      }



  queueForMatch(){
    this.matchmakingservice.queueMatch().subscribe(data => {
      console.log(data)
    })
  }

  showNotification(message: string){
    alert(message);
  }


}



