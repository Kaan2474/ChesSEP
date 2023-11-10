import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../Service/friends.service";



@Component({
  selector: 'app-friendslist',
  templateUrl: './friendslist.component.html',
  styleUrls: ['./friendslist.component.css']
})
export class FriendslistComponent implements OnInit{
  constructor(private friendsService: FriendsService) { }
  ngOnInit() {
    this.getFriends()
  }

  getFriends() {
    this.friendsService.getFriendslist()
      .subscribe((data) => {
        console.log(data)
      });
  }
}
