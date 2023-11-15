import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {User} from "../../Modules/User";
import {ActivatedRoute} from "@angular/router";
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";

@Component({
  selector: 'app-friendlist-of-friends',
  templateUrl: './friendlist-of-friends.component.html',
  styleUrls: ['./friendlist-of-friends.component.css']
})
export class FriendlistOfFriendsComponent implements OnInit{
  id: any;
  user: User;
  allFriends: Friends[] = [];
  constructor(private userService: UserService, private route: ActivatedRoute, private friendService: FriendsService) {
    this.user = new User();
  }

  ngOnInit() {
    this.id = this.route.snapshot.params["id"];
    console.log('userId:', this.id);
    this.getUserData();
  }

  /*Gibt die Daten zurück für Vorname und Nachname*/
  getUserData() {
    this.userService.getUser(this.id)
      .subscribe(data => {
        this.user = data;
        this.onGetFriendlistOfFriend(this.user);
      })

  }


  onGetFriendlistOfFriend(user: User) {
    this.friendService.getFriendListOf(user.id)
      .subscribe(data => {
        this.allFriends = data;
      });
  }
}
