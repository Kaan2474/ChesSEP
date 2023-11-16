import {Component, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute} from "@angular/router";


@Component({
  selector: 'app-play-game-against-user',
  templateUrl: './play-game-against-user.component.html',
  styleUrls: ['./play-game-against-user.component.css']
})
export class PlayGameAgainstUserComponent implements OnInit {
  id:any;
  user: any;
  friend:any
  token = localStorage.getItem("JWT");
  url = "assets/images/profil-picture-icon.png"

  constructor(private userService: UserService,private route: ActivatedRoute) {
    this.user = new User();
    this.friend = new User();
  }
  ngOnInit() {
    this.getUserDetail();
    this.id = this.route.snapshot.params["id"];
    console.log('friendID:', this.id);
    this.getProfileFriend();
  }

  getUserDetail() {
    this.userService.getUserbyToken().subscribe((data) => {
        console.log(data)
        this.user = data

        if(this.user.profilbild!=null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
      },
      error => {
        console.error("Fehler beim Laden der Benutzerdaten");
      });
  }
  getProfileFriend(){
    this.userService.getUser(this.id).subscribe(data=> {
        this.friend = data;
        console.log(this.friend)
        if(this.friend.profilbild!=null) {
          this.friend.profilbild = 'data:image/png;base64,' + this.friend.profilbild;
        }
      },
      error => {
        console.error('Error getting user profile:', error);

      }
    );
  }
}
