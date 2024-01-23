import { Component } from '@angular/core';

@Component({
  selector: 'app-replay-moves',
  templateUrl: './replay-moves.component.html',
  styleUrls: ['./replay-moves.component.css']
})
export class ReplayMovesComponent {

  PGN: string = "1. e4 e5 2. d4 exd4 3. Qxd4 Nc6 4. Qe3 Nf6 5. Nc3 Bb4 6. Bd2 O-O 7. O-O-O Re8 8. Qg3 Rxe4 9. a3 Rg4 10. Qh3 Bc5 11. f3 Rd4 12. g4 d5 13. Bd3 Ne5 14. Bg5 h6 15. Bxf6 Qxf6 16. Nge2 Nxd3+ 17. cxd3 c6 18. Qg3 b5 19. h4 b4 20. g5 Qg6 21. Na4 Bd6 22. f4 c5 23. Nxd4 cxd4 24. axb4 Bxb4 25. Kb1 Bd7 26. h5 Qa6 27. gxh6 Qxh6 28. Rc1 Bxa4 29. Rhg1 Bd7 30. Rc7 Bf5 31. Rb7 a5 32. Ka1 1-0";
  moves: string[] = this.PGN.split(/\d+\./);
  currentChessMove: number;
  chessBoard: string[][];

  constructor() {
    this.removeBlanks();
    console.log(this.moves);
    this.currentChessMove = 0;
    //klein --> weiß
    //groß --> schwarz
    this.chessBoard =
    [
      ['r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'],
      ['p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      ['P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'],
      ['R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'],
    ];
  }

  ngAfterViewInit() {
    this.placeFigures();

  }

  //Entfernt die Leerzeichen am Anfang und Ende aus allen Elementen von moves
  removeBlanks() {
    for(let i = 0; i<this.moves.length; i++) {
      this.moves[i] = this.moves[i].trim();
    }
  }

  getPositions(notation: string) {
    let i;
    let j;
    const positions: number[] = [];
    i = notation.charAt(1);
    j = notation.charAt(0);
    switch(j) {
      case "a":
        j = 0;
        break;
      case "b":
        j = 1;
        break;
      case "c":
        j = 2;
        break;
      case "d":
        j = 3;
        break;
      case "e":
        j = 4;
        break;
      case "f":
        j = 5;
        break;
      case "g":
        j = 6;
        break;
      case "h":
        j = 7;
        break;
    }
    i = Number(i) - 1;
    j = Number(j);
    positions.push(i);
    positions.push(j);
    return positions;
  }

  //Gibt die Notation zurück anhand der Zeile und Spalte von der Matrix
  getNotation(number1: number, number2: number) {
    let notation = "";
    switch(number2) {
      case 0:
        notation += "a";
        break;
      case 1:
        notation += "b"
        break;
      case 2:
        notation += "c"
        break;
      case 3:
        notation += "d"
        break;
      case 4:
        notation += "e"
        break;
      case 5:
        notation += "f"
        break;
      case 6:
        notation += "g"
        break;
      case 7:
        notation += "h"
        break;
    }
    notation += number1 + 1;
    return notation;
  }


  //Platziert alle Figuren anhand der chessBoard Matrix
  placeFigures() {
    let field;
    for(let i = 0; i<this.chessBoard.length; i++) {
      for(let j = 0; j<this.chessBoard[i].length; j++) {
        //Weiße Figuren
        if(this.chessBoard[i][j] === "p") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/bauer_w.png)";
        }
        else if(this.chessBoard[i][j] === "r") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/turm_w.png)";
        }
        else if(this.chessBoard[i][j] === "n") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/springer_w.png)";
        }
        else if(this.chessBoard[i][j] === "b") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/laeufer_w.png)";
        }
        else if(this.chessBoard[i][j] === "q") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/dame_w.png)";
        }
        else if(this.chessBoard[i][j] === "k") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/koenig_w.png)";
        }

        //Schwarze Figuren
        else if(this.chessBoard[i][j] === "P") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/bauer_s.png)";
        }
        else if(this.chessBoard[i][j] === "R") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/turm_s.png)";
        }
        else if(this.chessBoard[i][j] === "N") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/springer_s.png)";
        }
        else if(this.chessBoard[i][j] === "B") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/laeufer_s.png)";
        }
        else if(this.chessBoard[i][j] === "Q") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/dame_s.png)";
        }
        else if(this.chessBoard[i][j] === "K") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/koenig_s.png)";
        }
        //Keine Figuren
        else {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "none";
        }
      }
    }
  }

  nextChessMove() {
    if(this.currentChessMove < this.moves.length - 1) {
      this.currentChessMove++;
      this.evaluateMoves();
    }
  }

  prevChessMove() {
    if(this.currentChessMove !== 0) {
      this.currentChessMove--;
      this.evaluateMoves();
    }
  }

  //Teilt die weißen und die schwarzen Züge auf
  splitMoves(currentChessMove: number) {
    const splittedMoves: string[] = [];
    let move = this.moves[currentChessMove];
    let whiteMove = "";
    let blackMove = "";

    let i = 0;
    while(move.charAt(i) !== " ") {
      whiteMove += move.charAt(i);
      i++;
    }
    while(i<move.length) {
      blackMove += move.charAt(i);
      i++;
    }
    blackMove = blackMove.trim();
    splittedMoves.push(whiteMove);
    splittedMoves.push(blackMove);
    return splittedMoves;
  }


  evaluateMoves() {
    const splittedMoves = this.splitMoves(this.currentChessMove);
    console.log(splittedMoves[0]);
    console.log(splittedMoves[1]);
    //Weiße Figuren
    if(splittedMoves[0].charAt(0) === "K") {

    }
    else if(splittedMoves[0].charAt(0) === "Q") {

    }
    else if(splittedMoves[0].charAt(0) === "R") {

    }
    else if(splittedMoves[0].charAt(0) === "B") {

    }
    else if(splittedMoves[0].charAt(0) === "N") {

    }
    else if(splittedMoves[0].charAt(0) === "O") {

    }
    else {
      let positions = this.getPositions(splittedMoves[0]);
      this.updateChessBoard(positions[0], positions[1], "p");
      this.removeWhiteFigures(positions[0], positions[1], "p")
      console.log(this.chessBoard);
    }

    //Schwarze Figuren
    if(splittedMoves[1].charAt(0) === "K") {

    }
    else if(splittedMoves[1].charAt(0) === "Q") {

    }
    else if(splittedMoves[1].charAt(0) === "R") {

    }
    else if(splittedMoves[1].charAt(0) === "B") {

    }
    else if(splittedMoves[1].charAt(0) === "N") {

    }
    else if(splittedMoves[1].charAt(0) === "O") {

    }
    else {
      if(this.pawnBeaten(splittedMoves[1])) {
        console.log(1);
      }
      else {
        let positions = this.getPositions(splittedMoves[1]);
        this.updateChessBoard(positions[0], positions[1], "P");
        this.removeBlackFigures(positions[0], positions[1], "P")
      }
      console.log(this.chessBoard);
    }
    this.placeFigures();
  }


  pawnBeaten(move: string) {
    if(move.includes("x")) {

      return true;
    }
    return false;
  }

  updateChessBoard(i: number,j: number, figure:string) {
    this.chessBoard[i][j] = figure;
  }

  removeWhiteFigures(i: number, j: number, figure: string) {
    if(figure === "p") {
      if(this.chessBoard[i-1][j] === "p") {
        this.chessBoard[i-1][j] = " ";
      }
      else if(this.chessBoard[i-2][j] === "p") {
        this.chessBoard[i-2][j] = " ";
      }
    }
  }


  removeBlackFigures(i: number, j: number, figure: string) {
    if(figure === "P") {
      if(this.chessBoard[i+1][j] === "P") {
        this.chessBoard[i+1][j] = " ";
      }
      else if(this.chessBoard[i+2][j] === "P") {
        this.chessBoard[i+2][j] = " ";
      }
    }
  }
}


