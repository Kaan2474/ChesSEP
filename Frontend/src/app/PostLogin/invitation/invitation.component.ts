import { Component, OnInit } from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Friends} from "../../Modules/Friends";

@Component({
  selector: 'app-invitation',
  templateUrl: './invitation.component.html',
  styleUrls: ['./invitation.component.css']
})
export class InvitationComponent implements OnInit {
  activeView = "friends";

  user:User;
  friends:any;
  friendsList:Friends[]=[];

  constructor(private friendService: FriendsService,
              private userService: UserService,
              private router: Router,
              private route: ActivatedRoute) {this.user = new User()
  }
  ngOnInit() {
    if(localStorage.getItem("ActiveUser") === "" || localStorage.getItem("ActiveUser") === undefined){
      this.router.navigate([""]);
    }
    this.user.email = localStorage.getItem("ActiveUser");
    this.userService.getUser(this.user.id).subscribe( res => {
      this.user = res;
      this.friendService.getFriendRequest(res.id).subscribe(      res => this.friendsList = res
      )});
  }

  // Lade die Freundschaftsanfragen vom Backend
  loadFriendRequests() {
    this.friendService.getFriendRequest(this.user).subscribe(
      res => this.friendsList = res
    );

  }

  // Anfrage annehmen
  acceptFriendRequest(friendID: any) {
    this.friendService.acceptRequest(this.user, friendID).subscribe();
  }

  // Anfrage ablehnen
  declineFriendRequest(friendID: any) {
    this.friendService.denyRequest(this.user,friendID).subscribe();
  }
}
