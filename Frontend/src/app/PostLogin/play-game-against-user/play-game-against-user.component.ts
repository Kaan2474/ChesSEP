import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {Chess} from "../../Modules/Chess";
import {Subscription, interval, of} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {style} from "@angular/animations";


@Component({
  selector: 'app-play-game-against-user',
  templateUrl: './play-game-against-user.component.html',
  styleUrls: ['./play-game-against-user.component.css']
})
export class PlayGameAgainstUserComponent implements OnInit,OnDestroy {
  id:any;
  user: User;
  rival: User;
  token = localStorage.getItem("JWT");
  url = "assets/images/profil-picture-icon.png";

  chessGame : any= new Chess();
  currentBoard: any;
  lastHighlight: number = -1;
  lastPosition: string = "";
  zugID = 0;
  timer: number[] = [];


  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private matchmakinService: MatchmakingService,
    private http: HttpClient,
    private router: Router) {
      this.user = new User();
      this.rival = new User();
    }

  sub:Subscription = new Subscription;

  ngOnInit() {
    this.getMyCurrentMatch();
    this.getUserDetail();
    this.getIdOfRival();
    this.OnGetCurrentFrame();

    this.id = this.route.snapshot.params["id"];

    this.refreshMatch();
  }

  ngOnDestroy(){
    this.sub.unsubscribe();
  }



  refreshMatch() {
    this.sub = interval(500).subscribe(data => {
      this.matchmakinService.getMyCurrentMatch().subscribe(chess => {
        if(chess==null){
         this.router.navigate(["/homepage"]);
        }
      });
      this.OnGetCurrentFrame();
    });
  }

  getUserDetail() {
    this.userService.getUserbyToken().subscribe(data => {
        this.user = data;
        if(this.user.profilbild != null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
      },
      error => {
        console.error("Fehler beim Laden der Benutzerdaten");
      });
  }

  //Gibt die ID des Gegners zurück
  getIdOfRival(){
    this.matchmakinService.getMyCurrentEnemy().subscribe(data=> {
      this.rival.id = data;
      this.getRival();
    })
  }

  //Speichert den Gegner in der Variable rival anhand der ID
  getRival(){
    this.userService.getUser(this.rival.id).subscribe(data=> {
      this.rival = data;
      if(this.rival.profilbild!=null) {
        this.rival.profilbild = 'data:image/png;base64,' + this.rival.profilbild;
      }
    }
  );
  }

  getMyCurrentMatch(){
    this.matchmakinService.getMyCurrentMatch().subscribe(data =>{
      this.chessGame = data;
      console.log(this.chessGame);
    })
  }

  endGame(){
    this.matchmakinService.endMyMatch().subscribe();
    alert("Du hast das Spiel aufgegeben. Das Spiel wurde beendet!");
    this.router.navigate(["/homepage"]);
  }

  /*Gibt das aktuelle Spielfeld aus*/
  OnGetCurrentFrame() {
    this.matchmakinService.getCurrentFrame(this.zugID).subscribe(data => {
      let board: any = data;
      if(board.length === 0) {
        return;
      }
      this.currentBoard = data;
      this.checkForWinner();
      this.zugID = this.currentBoard[0][0][0];
        console.log(this.currentBoard);
      this.placeFigures(this.currentBoard);
      this.timer[0] = (this.currentBoard[0][1][1] / 1000 / 60);
      this.timer[1] = (this.currentBoard[0][1][1] / 1000 / 60);
    })
  }

  /*Speichert die IDs der Buttons in ein Array*/
  translateCoordinatesFromNotation(notation: string) {
    let numbers = [];
    switch(notation.charAt(0)) {
      case 'a':
        numbers[1] = 0;
        break;
      case 'b':
        numbers[1] = 1;
        break;
      case 'c':
        numbers[1] = 2;
        break;
      case 'd':
        numbers[1] = 3;
        break;
      case 'e':
        numbers[1] =4;
        break;
      case 'f':
        numbers[1] =5;
        break;
      case 'g':
        numbers[1] =6;
        break;
      case 'h':
        numbers[1] =7;
        break;
    }
    numbers[0] = 8 - Number(notation.charAt(1));
    return numbers;
  }

  /*Speichert die IDs der Buttons in ein Array*/
  translateNotationFromCoordinates(cords: number[]) {
    let notation = "";
    switch(cords[1]) {
      case 0:
        notation += "a";
        break;
      case 1:
        notation += "b";
        break;
      case 2:
        notation += "c";
        break;
      case 3:
        notation += "d";
        break;
      case 4:
        notation += "e";
        break;
      case 5:
        notation += "f";
        break;
      case 6:
        notation += "g";
        break;
      case 7:
        notation += "h";
        break;
    }
    notation += String(8 - (cords[0]));
    return notation;
  }


  //Platziert die Figuren
  placeFigures(board: number[][][]) {
    let field;
    let numbers: number[] = [];

    for(let i = 0; i<board[1].length; i++) {
      for(let j = 0; j<board[1][i].length; j++) {
        numbers[0] = i;
        numbers[1] = j;
        if(board[1][i][j] === 1) {
          if(board[2][i][j] === 1) {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/bauer_w.png)";
          }
          else {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/bauer_s.png)";
          }
        }
        else if(board[1][i][j] === 2) {
          if(board[2][i][j] === 1) {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/turm_w.png)";
          }
          else {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/turm_s.png)";
          }
        }
        else if(board[1][i][j] === 3) {
          if(board[2][i][j] === 1) {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/springer_w.png)";
          }
          else {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/springer_s.png)";
          }
        }
        else if(board[1][i][j] === 4) {
          if(board[2][i][j] === 1) {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/laeufer_w.png)";
          }
          else {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/laeufer_s.png)";
          }
        }
        else if(board[1][i][j] === 5) {
          if(board[2][i][j] === 1) {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/dame_w.png)";
          }
          else {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/dame_s.png)";
          }
        }
        else if(board[1][i][j] === 6) {
          if(board[2][i][j] === 1) {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/koenig_w.png)";
          }
          else {
            field = document.getElementById(this.translateNotationFromCoordinates(numbers));
            field!.style.backgroundImage = "url(assets/figures/koenig_s.png)";
          }
        }
        else {
          field = document.getElementById(this.translateNotationFromCoordinates(numbers));
          field!.style.backgroundImage = "none";
        }
      }
    }
  }

  showHighlight(cords: string) {
    this.clearAll();
    let coordinates = this.translateCoordinatesFromNotation(cords);
    let index = this.currentBoard[6][coordinates[0]][coordinates[1]];
    if(index == 0) {
      return;
    }
    for(let i = 0; i<this.currentBoard[index].length;i++) {
      for(let j = 0; j<this.currentBoard[index][i].length; j++) {
        if(this.currentBoard[index][i][j] != 0) {
          this.setFieldColour(this.translateNotationFromCoordinates([i,j]));
        }
      }
    }
    this.lastPosition = String(coordinates[0]) + String(coordinates[1]);
    this.lastHighlight = index;
  }

  buttonManager(cords: string) {
    this.clearAll();
    this.showHighlight(cords);
    this.makeMove(cords);
  }

  setFieldColour(notation: string) {
    let field = document.getElementById(notation);
    field!.style.border = "solid 3px green";
  }

  clearAll() {
    for(let i = 0; i<8; i++) {
      for(let j = 0; j<8; j++) {
        let field = document.getElementById(this.translateNotationFromCoordinates([i,j]));
        field!.style.border = "solid 0px black"
      }
    }
  }

  makeMove(cords: string) {
    if(this.lastHighlight === -1) {
      return;
    }
    let coordinates = this.translateCoordinatesFromNotation(cords);
    if(this.currentBoard[this.lastHighlight][coordinates[0]][coordinates[1]] === 1) {
      this.matchmakinService.makeAmove(Number(this.lastPosition), (coordinates[0] * 10) + coordinates[1]).subscribe();
    }
  }

  checkForWinner() {
    if(this.currentBoard[0][2][0] === 1) {
      alert("Spieler weiß hat gewonnen!");
    }
    else if(this.currentBoard[0][2][0] === 2) {
      alert("Spieler schwarz hat gewonnen!");
    }
    else if(this.currentBoard[0][2][0] === 3) {
      alert("Unentschieden!");
    }
  }

}
