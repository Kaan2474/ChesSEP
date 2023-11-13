import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {User} from "../../Modules/User";




@Component({
  selector: 'app-friendslist',
  templateUrl: './friendslist.component.html',
  styleUrls: ['./friendslist.component.css']
})
export class FriendslistComponent implements OnInit{
  public allFriends: User[] = [];
  constructor(private friendsService: FriendsService) { }
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
}
