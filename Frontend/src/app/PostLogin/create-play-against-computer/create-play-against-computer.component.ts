import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {FriendsService} from "../../Service/friends.service";
import {UserService} from "../../Service/user.service";
import {Friends} from "../../Modules/Friends";
import {Chess} from "../../Modules/Chess";

@Component({
  selector: 'app-create-play-against-computer',
  templateUrl: './create-play-against-computer.component.html',
  styleUrls: ['./create-play-against-computer.component.css']
})
export class CreatePlayAgainstComputerComponent implements OnInit{
  selectedDifficulty: any;

  URL = "http://localhost:8080/match";

  token = localStorage.getItem("JWT");

  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

  chessGame:any; //soll einfach die JSON file in der Console anzeigen {"gameid", "match-length", "name", "blackid", "whiteid", "timeStamp"}
  allChessGames:any;
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

    switch (selectedDifficulty) {
      case 'Anfänger':
        this.selectedDifficulty = 0;
        break;
      case 'Fortgeschritten':
        this.selectedDifficulty = 1;
        break;
      default:
        this.selectedDifficulty = 2;  // Rückgabe eines Standardwerts oder Fehlerbehandlung
    }
      this.matchmakingservice.difficulty(this.selectedDifficulty).subscribe(data=> {
        this.selectedDifficulty = data;
    this.router.navigate(["/play-game-against-user"]);}
      )

  }

  waitForMatch(chess: Chess) {
    this.router.navigate(["/play-game-against-user"]);
  }
}
