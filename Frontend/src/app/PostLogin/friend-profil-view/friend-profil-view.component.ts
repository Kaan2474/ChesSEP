import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";
import {User} from "../../Modules/User";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-friend-profil-view',
  templateUrl: './friend-profil-view.component.html',
  styleUrls: ['./friend-profil-view.component.css']
})
export class FriendProfilViewComponent implements OnInit {

  id: any;
  user: User;
  token = localStorage.getItem("JWT");

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.user = new User()

  }
  ngOnInit() {
    this.id = this.route.snapshot.params["id"];
    console.log('userId:', this.id);
    this.getProfileFriend();

}
  getProfileFriend(){
    this.userService.getUser(this.id).subscribe(data=> {
      this.user = data;
      console.log(this.user)
      if(this.user.profilbild!=null) {
        this.user.profilbild = 'data:image/png;base64,' + this.user.profilbild;
      }
      },
      error => {
        console.error('Error getting user profile:', error);

      }
    );
  }
}



