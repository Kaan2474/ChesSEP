import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-hidegame',
  templateUrl: './hidegame.component.html',
  styleUrls: ['./hidegame.component.css']
})
export class HidegameComponent {
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

