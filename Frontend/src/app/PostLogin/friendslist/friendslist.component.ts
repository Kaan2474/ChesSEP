import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute} from "@angular/router";


@Component({
  selector: 'app-friendslist',
  templateUrl: './friendslist.component.html',
  styleUrls: ['./friendslist.component.css']
})
export class FriendslistComponent implements OnInit{
  public allFriends: Friends[] = [];
  user: User;
  privacyText: any;



  constructor(private friendsService: FriendsService, private userService: UserService, private route: ActivatedRoute) {
    this.user = new User();
    this.refreshUser();
  }
  ngOnInit() {
    this.getFriends();
    this.refreshUser();
  }

  /*Gibt den Token des Users zurück und speichert diesen in der Variable user*/
  refreshUser() {
    this.userService.getUserbyToken()
      .subscribe(data => {
        this.user = data;
        this.setPrivacy();
      });
    
  }


  /*Zeigt die Freundesliste an*/
  getFriends() {
    this.friendsService.getFriendslist()
      .subscribe((data) => {
        console.log(data)
        this.allFriends = data;
      });
  }

  /*Prüft, ob Freundesliste privat oder öffentlich ist */
  setPrivacy() {
    this.privacyText = "Status der Privatsphäre: "+this.user.privacy;
  }

  /*Ändert die Privatsphäre des Users*/
  changePrivacy() {
    this.userService.putPrivacy()
      .subscribe(data => {
        this.refreshUser();
      })
    }

    onDeleteFriend(friend: {id: number}) {
    this.friendsService.deleteFriend(friend)
      .subscribe(data => {
        console.log(1);
      })
    }
}
