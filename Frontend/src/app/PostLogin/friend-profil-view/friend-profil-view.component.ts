import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {UserService} from "../../Service/user.service";
import {User} from "../../Modules/User";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-friend-profil-view',
  templateUrl: './friend-profil-view.component.html',
  styleUrls: ['./friend-profil-view.component.css']
})
export class FriendProfilViewComponent implements OnInit {

  user: User;
  userId: any;
  token = localStorage.getItem("JWT");

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.user = new User();

  }

  ngOnInit() {
    this.route.params.subscribe(params=>{
    const userId = params["userId"];
    this.getProfileFriend(userId);

  });

}

  getProfileFriend(userId: any){
    this.userService.getUser(userId).subscribe(data=> {
      console.log(data)
      this.user = data;
    })
  }
}

