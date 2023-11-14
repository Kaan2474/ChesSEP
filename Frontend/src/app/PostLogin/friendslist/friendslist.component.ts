import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";


@Component({
  selector: 'app-friendslist',
  templateUrl: './friendslist.component.html',
  styleUrls: ['./friendslist.component.css']
})
export class FriendslistComponent implements OnInit{
  public allFriends: Friends[] = [];
  user: User;
  constructor(private friendsService: FriendsService, private userService: UserService) {
    this.user = new User();
    this.token();
  }
  ngOnInit() {
    this.getFriends();
  }

  token() {
    this.userService.getUserbyToken()
      .subscribe(data => {
        this.user = data;
      });
  }

  getFriends() {
    this.friendsService.getFriendslist()
      .subscribe((data) => {
        console.log(data)
        this.allFriends = data;
      });
  }

  changeToPrivate() {
    this.userService.putPrivacy(this.user)
      .subscribe(data => {
        console.log(data);
      })
    }
}
