import { Component, OnInit } from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Friends} from "../../Modules/Friends";
import {MatchmakingService} from "../../Service/matchmaking.service";

@Component({
  selector: 'app-invitation',
  templateUrl: './invitation.component.html',
  styleUrls: ['./invitation.component.css']
})
export class InvitationComponent implements OnInit {
  user:User;
  friendsList:Friends[]=[];
  matchList: Friends[]=[];

  constructor(private friendService: FriendsService,
              private userService: UserService,
              private matchmakingService:MatchmakingService,
              private router: Router,
              private route: ActivatedRoute) {this.user = new User()
  }
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
  }

  // Anfrage annehmen
  acceptFriendRequest(friend: any) {
    this.friendService.acceptRequest(this.user, friend).subscribe();
  }

  // Anfrage ablehnen
  declineFriendRequest(friend: any) {
    this.friendService.denyRequest(this.user,friend).subscribe();
  }
  acceptMatchRequest(friend:any){
    this.matchmakingService.acceptMatchRequest(this.user,friend).subscribe()
    }
  declineMatchRequest(friend: any) {
    this.matchmakingService.denyMatchRequest(this.user,friend).subscribe();
  }

}
