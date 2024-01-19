import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {Chess} from "../../Modules/Chess";
import {Subscription, interval, of} from 'rxjs';
@Component({
  selector: 'app-streaming-ansicht',
  templateUrl: './streaming-ansicht.component.html',
  styleUrls: ['./streaming-ansicht.component.css']
})
export class StreamingAnsichtComponent implements OnInit,OnDestroy{
  gameID:any;
  playerID:any;
  rivalID:any
  id:any;
  user: User;
  rival: User;
  token = localStorage.getItem("JWT");
  url = "assets/images/profil-picture-icon.png";

  chessGame : any= new Chess();
  currentBoard: any;
  lastHighlight: number[][] = [];
  lastPosition: string = "";
  zugID = -1;
  timer: number[] = [];
  bauerTransform:boolean=false;
  PlayerColor:number=1;


  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private matchmakinService: MatchmakingService,
    private router: Router) {
    this.user = new User();
    this.rival = new User();
    this.bauerTransform=false;
  }

  sub:Subscription = new Subscription;
  interval: Subscription = new Subscription;

  ngOnInit() {
    this.userService.getUserbyToken().subscribe(data=> this.user = data);
    this.gameID = localStorage.getItem("GameID");
    this.playerID = localStorage.getItem("PlayerID");
    this.rivalID = localStorage.getItem("GegnerplayerID")
    console.log("GameID: " + this.gameID);
    console.log("GameID: " + this.playerID);

    this.getUserDetail();
    this.getRival();
    this.OnGetCurrentFrame();


    this.id = this.route.snapshot.params["id"];
  this.refreshMatch();
  }

  ngOnDestroy(){
    this.sub.unsubscribe();
  }



  refreshMatch() {
    this.sub = interval(500).subscribe(data => {
      this.matchmakinService.joinStreamMatch(this.gameID, this.playerID, this.zugID).subscribe(chess => {;
        if(chess==null){
          this.router.navigate(["/homepage"]);
        }
      });
      this.OnGetCurrentFrame();
    });
  }

  getUserDetail() {
    this.userService.getUser(this.playerID).subscribe(data => {
        this.user = data;
        if(this.user.profilbild != null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
        this.getMyCurrentMatch();
      },
      error => {
        console.error("Fehler beim Laden der Benutzerdaten");
      });
  }


  //Speichert den Gegner in der Variable rival anhand der ID
  getRival(){
    this.userService.getUser(this.rivalID).subscribe(data=> {
        this.rival = data;
        if(this.rival.profilbild!=null) {
          this.rival.profilbild = 'data:image/png;base64,' + this.rival.profilbild;
        }
      }
    );
  }

  getMyCurrentMatch(){
    this.matchmakinService.joinStreamMatch(this.gameID, this.playerID, this.zugID).subscribe(data => {;
      this.chessGame = data;
      console.log(this.chessGame);
      if(this.chessGame.playerWhiteID==this.user.id){
        this.PlayerColor=1;
      }else{
        this.PlayerColor=2;
      }
    })
  }

  endGame(){
    this.matchmakinService.surrender().subscribe();
    alert("Du hast das Spiel aufgegeben. Das Spiel wurde beendet!");
    //this.router.navigate(["/homepage"]);
  }

  /*Gibt das aktuelle Spielfeld aus*/
  OnGetCurrentFrame() {
      this.matchmakinService.joinStreamMatch(this.gameID, this.playerID, this.zugID).subscribe(data => {;

      if((data as number[][][]).length < 2) {
        var currentStatus:number[][][]=data as number[][][];
        this.currentBoard[0]=currentStatus[0];
        this.setTimer();
        this.checkForWinner();
        return;
      }

      this.currentBoard = data;
      console.log(this.currentBoard);
      this.setTimer();
      this.checkForWinner();
      this.zugID = this.currentBoard[0][0][0];
      this.interval.unsubscribe();

      this.placeFigures(this.currentBoard);
      this.clearAll();
      this.showLastMove();
      this.showCheck()
      this.checkForBauerTransform();
    })
  }

  /*Gibt die Position der Notation zurück
  * z.B: Notation: a1 --> Position 7 0*/
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
        numbers[1] = 4;
        break;
      case 'f':
        numbers[1] = 5;
        break;
      case 'g':
        numbers[1] = 6;
        break;
      case 'h':
        numbers[1] = 7;
        break;
    }
    numbers[0] = 8 - Number(notation.charAt(1));
    return numbers;
  }

  /*Gibt anhand der Position die Notation zurück*/
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


  //Platziert die Figuren auf das Spielfeld
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

  checkForBauerTransform(){
    var bauerT=this.currentBoard[4];

    for (let i = 0; i < bauerT.length; i++) {
      for (let j = 0; j < bauerT[i].length; j++) {
        if(bauerT[i][j]!=0&&this.currentBoard[2][i][j]==this.PlayerColor){
          this.setBorder(this.translateNotationFromCoordinates([i,j]),"blue")
          this.bauerTransform=true;
        }
      }
    }
  }

  /*Zeigt an, in welche Felder sich eine Figur bewegen kann, wenn man auf eine Figur klickt*/
  showHighlight(notation: string):boolean {
    let coordinates = this.translateCoordinatesFromNotation(notation);
    let index = this.currentBoard[6][coordinates[0]][coordinates[1]];

    if(index == 0) {
      return false;
    }

    for(let i = 0; i<this.currentBoard[index].length;i++) {
      for(let j = 0; j<this.currentBoard[index][i].length; j++) {
        if(this.currentBoard[index][i][j] != 0) {
          this.setBorder(this.translateNotationFromCoordinates([i,j]),"green");
        }
      }
    }

    this.lastPosition = String(coordinates[0]) + String(coordinates[1]);
    this.lastHighlight = this.currentBoard[index];
    return true;
  }

  buttonManager(notation: string) {
    this.clearAll();
    this.showLastMove();
    this.showCheck();
    this.checkForBauerTransform();
    if(!this.showHighlight(notation)&&!this.bauerTransform){
      this.makeMove(notation);
    }
  }

  doTransformBauer(id:number){
    this.matchmakinService.transformBauer(id).subscribe(()=>{
      this.bauerTransform=false;
    })
  }

  showLastMove(){
    var lastMove=this.currentBoard[5];

    for (let i = 0; i < lastMove.length; i++) {
      for (let j = 0; j < lastMove[i].length; j++) {
        if(lastMove[i][j]!=0)
          this.setBorder(this.translateNotationFromCoordinates([i,j]),"yellow")
      }
    }
  }

  /*Signalisiert, ob der König angegriffen wird bzw. in Gefahr ist*/
  showCheck(){
    var lastMove=this.currentBoard[3];

    for (let i = 0; i < lastMove.length; i++) {
      for (let j = 0; j < lastMove[i].length; j++) {
        if(lastMove[i][j]!=0)
          this.setBorder(this.translateNotationFromCoordinates([i,j]),"red")
      }
    }
  }

  /*Fügt einen Border an die Felder hinzu, in die eine Figur sich bewegen kann*/
  setBorder(notation: string,color:string) {
    let field = document.getElementById(notation);
    field!.style.border = "solid 3px "+color;
  }

  /*Entfernt die Border an den Feldern*/
  clearAll() {
    for(let i = 0; i<8; i++) {
      for(let j = 0; j<8; j++) {
        let field = document.getElementById(this.translateNotationFromCoordinates([i,j]));
        field!.style.border = "solid 0px black"
      }
    }
  }

  /*Bewegt eine Figur in ein Feld, welches man anklickt
  * Bedingung: Feld muss einen Border haben*/
  makeMove(notation: string) {
    if(this.lastHighlight.length==0) {
      return;
    }

    let coordinates = this.translateCoordinatesFromNotation(notation);
    if(this.lastHighlight[coordinates[0]][coordinates[1]] === 1) {
      this.matchmakinService.makeAmove(Number(this.lastPosition), (coordinates[0] * 10) + coordinates[1]).subscribe(data => {
        if(data === false) {
          alert("Das ist nicht der bestmögliche Zug!");
        }
      });
    }
  }

  /*Prüft, ob das Spiel zu Ende ist*/
  checkForWinner() {
    if(this.currentBoard[0][2][0] === 1) {
      alert("Spieler weiß hat gewonnen!");
      this.router.navigate(["/homepage"]);
    }
    else if(this.currentBoard[0][2][0] === 2) {
      alert("Spieler schwarz hat gewonnen!");
      this.router.navigate(["/homepage"]);
    }
    else if(this.currentBoard[0][2][0] === 3) {
      alert("Unentschieden!");
      this.router.navigate(["/homepage"]);
    }
    else if((this.currentBoard[0][1][0] < 0 || this.currentBoard[0][1][1] < 0)&&this.currentBoard[0][3][0]==0) {
      alert("Der Timer ist abgelaufen");
      this.router.navigate(["/homepage"]);
    }

  }

  setTimer() {
    if(this.PlayerColor==1) {
      this.timer[1]=Math.round(this.currentBoard[0][1][1]/1000);
      this.timer[0]=Math.round(this.currentBoard[0][1][0]/1000);
    }
    else {
      this.timer[0]=Math.round(this.currentBoard[0][1][1]/1000);
      this.timer[1]=Math.round(this.currentBoard[0][1][0]/1000);
    }
  }
}


