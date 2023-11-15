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
  check: any;
  privacyText: any;



  constructor(private friendsService: FriendsService, private userService: UserService, private route: ActivatedRoute) {
    this.user = new User();
    this.myToken();
  }
  ngOnInit() {
    this.getFriends();
    this.checkPrivacy();
  }

  /*Gibt den Token des Users zurück und speichert diesen in der Variable user*/
  myToken() {
    this.userService.getUserbyToken()
      .subscribe(data => {
        this.user = data;
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
  checkPrivacy() {
    if(this.check == "OEFFENTLICH") {
      this.privacyText = "Status der Privatsphäre: Öffentlich";
    }
    else {
      this.privacyText = "Status der Privatsphäre: Privat";
    }
  }

  /*Ändert die Privatsphäre des Users*/
  changePrivacy() {
    this.userService.putPrivacy()
      .subscribe(data => {
        this.myToken()
        this.check = this.user.privacy;
        console.log(this.check);
      })
      this.checkPrivacy();
    }

    onDeleteFriend(friend: {id: number}) {
    this.friendsService.deleteFriend(friend)
      .subscribe(data => {
        console.log(1);
      })
    }
}
