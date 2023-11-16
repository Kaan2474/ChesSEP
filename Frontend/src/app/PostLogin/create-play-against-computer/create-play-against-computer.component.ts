import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-create-play-against-computer',
  templateUrl: './create-play-against-computer.component.html',
  styleUrls: ['./create-play-against-computer.component.css']
})
export class CreatePlayAgainstComputerComponent {
  gameName:string ='Spiel';
  selectedTimer: number = 5;
  selectedDifficulty: string = 'Anfänger';
  constructor(private router: Router) {}

  saveGameName(){
    this.gameName = (<HTMLInputElement>document.getElementById("gameNameInput")).value;
    localStorage.setItem("gameName", this.gameName);
  }

  startGame() {
    this.router.navigate(['/play-game-against-computer'], {
      queryParams: {
        timer: this.selectedTimer,
        difficulty: this.selectedDifficulty,
      }
    });
  }
}
