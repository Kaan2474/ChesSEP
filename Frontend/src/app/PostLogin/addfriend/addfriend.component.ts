import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { MatchmakingService } from 'src/app/Service/matchmaking.service';


@Component({
  selector: 'app-addfriend',
  templateUrl: './addfriend.component.html',
  styleUrls: ['./addfriend.component.css']
})
export class AddfriendComponent {
  URL = "http://localhost:8080/friend";
  token = localStorage.getItem("JWT");
  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

  constructor(private http: HttpClient, private matchmakingservice:MatchmakingService ) { }
  //Sendet die eingegebene Email im Formular an das Backend weiter
  onAddFriend(user: {email: string}) {
    console.log(user);
    this.http.post(this.URL + "/sendFriendRequest", user, {headers: this.header})
      .subscribe(data => {
        console.log(data)
      });
  }

  ngOnInit(){
    this.matchmakingservice.cancelMatchRequest().subscribe();
    this.matchmakingservice.dequeueMatch().subscribe();
  }

}
