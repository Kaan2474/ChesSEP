import {Component, OnInit} from '@angular/core';
import {MatchmakingService} from "../../Service/matchmaking.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserService} from "../../Service/user.service";
import {Friends} from "../../Modules/Friends";
import {Chess} from "../../Modules/Chess";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-play-against-computer',
  templateUrl: './create-play-against-computer.component.html',
  styleUrls: ['./create-play-against-computer.component.css']
})
export class CreatePlayAgainstComputerComponent implements OnInit{
  selectedDifficulty: any;
  user: any;


  constructor(private http: HttpClient,
              private matchmakingservice:MatchmakingService,
              private userService: UserService,
              private router: Router)
  {}

  ngOnInit() {
    this.userService.getUserbyToken().subscribe(data=>
      this.user = data);
  }

  queueForMatch(selectedDifficulty: any) {
    if (this.selectedDifficulty) {

    switch (selectedDifficulty) {
      case 'Anfänger':
        this.selectedDifficulty = 0;
        break;
      case 'Fortgeschritten':
        this.selectedDifficulty = 1;
        break;
      default:
        this.selectedDifficulty = 2;
    }
      this.matchmakingservice.difficulty(this.selectedDifficulty).subscribe(data=> {
        this.selectedDifficulty = data;
    this.router.navigate(["/play-game-against-user"]);
    console.log(selectedDifficulty)
      });
    } else {
      this.showNotification("Bitte wähle eine Schwierigkeitsstufe aus, um das Spiel zu starten.");
    }
  }

  showNotification(message: string) {
    alert(message);
  }

}
