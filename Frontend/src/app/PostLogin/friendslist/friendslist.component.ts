import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import {User} from "../../Modules/User";


@Component({
  selector: 'app-friendslist',
  templateUrl: './friendslist.component.html',
  styleUrls: ['./friendslist.component.css']
})
export class FriendslistComponent implements OnInit{
  public allFriends: Friends[] = [];
  user: User;
  constructor(private friendsService: FriendsService) {
    this.user = new User();
  }
  ngOnInit() {
    this.getFriends()
  }

  getFriends() {
    this.friendsService.getFriendslist()
      .subscribe((data) => {
        console.log(data)
        this.allFriends = data;
      });
  }

  changeToPrivate() {
    this.friendsService.putPrivacy(this.user)
      .subscribe(data => {
        console.log(data);
      })
    }
}
