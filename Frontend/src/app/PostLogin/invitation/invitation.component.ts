import { Component, OnInit } from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Friends} from "../../Modules/Friends";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-invitation',
  templateUrl: './invitation.component.html',
  styleUrls: ['./invitation.component.css']
})
export class InvitationComponent implements OnInit {
  user:User;
  friend:User;
  friendsList:Friends[]=[];
  matchList: Friends[]=[];
  token = localStorage.getItem("JWT");
  URL = "http://localhost:8080/match";

  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
  constructor(private http: HttpClient,
              private friendService: FriendsService,
              private userService: UserService,
              private matchmakingService:MatchmakingService,
              private router: Router,
              private route: ActivatedRoute,
              private matchmakingservice:MatchmakingService) {this.user = new User(),
                                              this.friend=new User()}
  ngOnInit() {
    if(localStorage.getItem("ActiveUser") == "" || localStorage.getItem("ActiveUser") == undefined){
      this.router.navigate([""]);
    }

    this.userService.getUserbyToken().subscribe( res => {
      this.user = res;
      this.friendService.getFriendRequest(res).subscribe(
        res => this.friendsList = res)
      this.matchmakingService.getMyMatchInvitations((res)).subscribe(
        res => this.matchList =res)
    });

    this.matchmakingservice.cancelMatchRequest().subscribe();
    this.matchmakingservice.dequeueMatch().subscribe();
  }

  acceptFriendRequest(friend: any) {
    this.friendService.acceptRequest(this.user, friend).subscribe(()=> {
      const index = this.friendsList.indexOf(friend);
      if (index !== -1) {
        this.friendsList.splice(index, 1);
      }
    });
    this.showNotification("Freundschaftsanfrage wurde erfolgreich angenommen")
  }

  declineFriendRequest(friend: any) {
    this.friendService.denyRequest(this.user,friend).subscribe(()=> {
      const index = this.friendsList.indexOf(friend);
      if (index !== -1) {
        this.friendsList.splice(index, 1);
      }
    });
    this.showNotification("Freundschaftsanfrage wurde erfolgreich abgelehnt")
  }
  acceptMatchRequest(friend:any){
    this.matchmakingService.acceptMatchRequest(this.user,friend).subscribe((data)=> {
      const index = this.friendsList.indexOf(friend);
      if (index !== -1) {
        this.friendsList.splice(index, 1);
      }
        this.myMatchRequest();
    });

  }
  myMatchRequest(){
    this.http.get(this.URL + "/getMyMatchRequest", {headers: this.header})
      .subscribe(data => {
        console.log(data)
        this.router.navigate(["/play-game-against-user"])
      });
  }
  declineMatchRequest(friend: any) {
    this.matchmakingService.denyMatchRequest().subscribe(()=> {
      const index = this.friendsList.indexOf(friend);
      if (index !== -1) {
        this.friendsList.splice(index, 1);
      }
    });
    this.showNotification("Spieleinladung wurde erfolgreich abgelehnt")
  }
  showNotification(message:string){
    alert(message);
  }
}
