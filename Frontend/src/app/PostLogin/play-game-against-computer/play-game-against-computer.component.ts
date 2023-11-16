import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import{UserService} from "../../Service/user.service";
import {FriendsService} from "../../Service/friends.service";
import {User} from "../../Modules/User";

@Component({
  selector: 'app-play-game-against-computer',
  templateUrl: './play-game-against-computer.component.html',
  styleUrls: ['./play-game-against-computer.component.css']
})
export class PlayGameAgainstComputerComponent {
  timer?: number; //Da Variable initialisiert werden muss => ?
  difficulty?: string;
  gameName?: string;
  id: any;
  user: User;
  token = localStorage.getItem("JWT");


  constructor(private userService: UserService, private route: ActivatedRoute) {
    this.route.queryParams.subscribe((params) => {
        this.timer = parseInt(params['timer']);
        this.difficulty = params['difficulty'];
      }
    )
    const storedGameName = localStorage.getItem("gameName");
    if (storedGameName) {
      this.gameName = storedGameName;
    };
    this.user = new User()
  }
  ngOnInit() {
    this.id = this.route.snapshot.params["id"];
    console.log('userId:', this.id);
    this.getUserDetail();
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
}

