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
  privacyText: string = "Die Privatsphäre ist derzeit auf öffentlich. Klicke den Button um die Privatsphäre zu ändern!"


  constructor(private friendsService: FriendsService, private userService: UserService) {
    this.user = new User();
    this.token();
    localStorage.setItem("Privacy", "OEFFENTLICH");
  }
  ngOnInit() {
    this.getFriends();
  }

  /*Gibt den Token des Users zurück und speichert diesen in der Variable user*/
  token() {
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

  /*Ändert die Privatsphäre des Users*/
  changeToPrivate() {
    if(localStorage.getItem("Privacy") === "OEFFENTLICH") {
      this.privacyText = "Die Privatsphäre ist derzeit auf privat. Klicke den Button um die Privatsphäre zu ändern!";
      localStorage.setItem("Privacy", "PRIVAT");
    }
    else {
      this.privacyText = "Die Privatsphäre ist derzeit auf öffentlich. Klicke den Button um die Privatsphäre zu ändern!";
      localStorage.setItem("Privacy", "OEFFENTLICH");
    }
    this.userService.putPrivacy(this.user)
      .subscribe(data => {
        console.log(data);
      })
    }
}
