import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-play-game-against-user',
  templateUrl: './play-game-against-user.component.html',
  styleUrls: ['./play-game-against-user.component.css']
})
export class PlayGameAgainstUserComponent {
  timer?: number; //Da Variable initialisiert werden muss => ?
  difficulty?: string;
  gameName?: string;

  constructor(private route: ActivatedRoute) {
    this.route.queryParams.subscribe((params) => {
        this.timer = parseInt(params['timer']);
        this.difficulty = params['difficulty'];
      }
    )
    const storedGameName = localStorage.getItem("gameName");
    if (storedGameName) {
      this.gameName = storedGameName;
    };
  }
}
