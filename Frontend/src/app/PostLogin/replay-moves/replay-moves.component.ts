import { Component } from '@angular/core';

@Component({
  selector: 'app-replay-moves',
  templateUrl: './replay-moves.component.html',
  styleUrls: ['./replay-moves.component.css']
})
export class ReplayMovesComponent {

  PGN: string = "1. e4 e5 2. d4 exd4 3. Qxd4 Nc6 4. Qe3 Nf6 5. Nc3 Bb4 6. Bd2 O-O 7. O-O-O Re8 8. Qg3 Rxe4 9. a3 Rg4 10. Qh3 Bc5 11. f3 Rd4 12. g4 d5 13. Bd3 Ne5 14. Bg5 h6 15. Bxf6 Qxf6 16. Nge2 Nxd3+ 17. cxd3 c6 18. Qg3 b5 19. h4 b4 20. g5 Qg6 21. Na4 Bd6 22. f4 c5 23. Nxd4 cxd4 24. axb4 Bxb4 25. Kb1 Bd7 26. h5 Qa6 27. gxh6 Qxh6 28. Rc1 Bxa4 29. Rhg1 Bd7 30. Rc7 Bf5 31. Rb7 a5 32. Ka1 1-0";
  allMoves: string[] = this.PGN.split(/\d+\./);
  currentChessMove: number = 0;
  currentChessBoard: number = 0;
  chessBoards: string[][][] = [];


  constructor() {
    this.removeBlanks();
    this.splitMoves();
    this.createFirstChessBoard();
    this.evaluateMoves();
  }

  ngAfterViewInit() {
    this.placeFigures(this.currentChessMove);
  }

  //Kreiert die ersten Chessboards
  createFirstChessBoard() {
    let startPosition: string[][] =
      //klein --> weiß
      //groß --> schwarz
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

    this.chessBoards.push(startPosition);
    console.log(this.chessBoards);
  }


  //Kopiert ein chessboard aus allen chessboards
  copyChessBoard(position: number) {
    let copy: string[][] =
      [
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
      [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    ];

    for(let i = 0; i<this.chessBoards[position].length; i++) {
      for(let j = 0; j<this.chessBoards[position][i].length; j++) {
        if(this.chessBoards[position][i][j] === "r") {
          copy[i][j] = "r";
        }
        else if(this.chessBoards[position][i][j] === "n") {
          copy[i][j] = "n";
        }
        else if(this.chessBoards[position][i][j] === "b") {
          copy[i][j] = "b";
        }
        else if(this.chessBoards[position][i][j] === "q") {
          copy[i][j] = "q";
        }
        else if(this.chessBoards[position][i][j] === "k") {
          copy[i][j] = "k";
        }
        else if(this.chessBoards[position][i][j] === "p") {
          copy[i][j] = "p";
        }
        else if(this.chessBoards[position][i][j] === "R") {
          copy[i][j] = "R";
        }
        else if(this.chessBoards[position][i][j] === "N") {
          copy[i][j] = "N";
        }
        else if(this.chessBoards[position][i][j] === "B") {
          copy[i][j] = "B";
        }
        else if(this.chessBoards[position][i][j] === "Q") {
          copy[i][j] = "Q";
        }
        else if(this.chessBoards[position][i][j] === "K") {
          copy[i][j] = "K";
        }
        else if(this.chessBoards[position][i][j] === "P") {
          copy[i][j] = "P";
        }
        else {
          copy[i][j] = " ";
        }
      }
    }
    return copy;
  }


  //Entfernt die Leerzeichen am Anfang und Ende aus allen Elementen von moves
  removeBlanks() {
    for(let i = 0; i<this.allMoves.length; i++) {
      this.allMoves[i] = this.allMoves[i].trim();
    }
  }

  //Gibt die Notation anhand der Position zurück --> Position = Zeile i und Spalte j
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

  //
  getPositions(notation: string) {
    const positions: number[] = [];
    positions.push(Number(notation[1]) - 1);
    switch(notation[0]) {
      case "a":
        positions.push(0);
        break;
      case "b":
        positions.push(1);
        break;
      case "c":
        positions.push(2);
        break;
      case "d":
        positions.push(3);
        break;
      case "e":
        positions.push(4);
        break;
      case "f":
        positions.push(5);
        break;
      case "g":
        positions.push(6);
        break;
      case "h":
        positions.push(7);
        break;
    }
    return positions;
  }


  //Platziert alle Figuren anhand des jeweiligen Zuges
  placeFigures(move: number) {
    let field;
    for(let i = 0; i<this.chessBoards[move].length; i++) {
      for(let j = 0; j<this.chessBoards[move][i].length; j++) {
        //Weiße Figuren
        if(this.chessBoards[move][i][j] === "p") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/bauer_w.png)";
        }
        else if(this.chessBoards[move][i][j] === "r") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/turm_w.png)";
        }
        else if(this.chessBoards[move][i][j] === "n") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/springer_w.png)";
        }
        else if(this.chessBoards[move][i][j] === "b") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/laeufer_w.png)";
        }
        else if(this.chessBoards[move][i][j] === "q") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/dame_w.png)";
        }
        else if(this.chessBoards[move][i][j] === "k") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/koenig_w.png)";
        }

        //Schwarze Figuren
        else if(this.chessBoards[move][i][j] === "P") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/bauer_s.png)";
        }
        else if(this.chessBoards[move][i][j] === "R") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/turm_s.png)";
        }
        else if(this.chessBoards[move][i][j] === "N") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/springer_s.png)";
        }
        else if(this.chessBoards[move][i][j] === "B") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/laeufer_s.png)";
        }
        else if(this.chessBoards[move][i][j] === "Q") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/dame_s.png)";
        }
        else if(this.chessBoards[move][i][j] === "K") {
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
    if(this.currentChessMove < this.allMoves.length) {
      console.log(this.currentChessMove);
      this.currentChessMove++;
      console.log(this.currentChessMove);
      this.placeFigures(this.currentChessMove);
    }
  }

  prevChessMove() {
    console.log(this.currentChessMove);
    this.currentChessMove--;
    console.log(this.currentChessMove);
    this.placeFigures(this.currentChessMove);
  }


  //Trennt die weißen und die schwarzen Züge auf
  splitMoves() {
    let temp: string[] = [];
    const splittedMoves: string[] = [];
    for(let i = 1; i<this.allMoves.length; i++) {
      //Trenne den Zug auf in weiß und schwarz(temp) und füge die Züge getrennt in splittedMoves hinzu
      temp = this.allMoves[i].split(" ");
      splittedMoves.push(temp[0]);
      splittedMoves.push(temp[1]);
    }
    //Verändere die globale Variable allMoves, in welcher alle Züge nun getrennt sind
    this.allMoves = splittedMoves;
    console.log(this.allMoves);
  }

  getTurn() {
    let decideTurn =  this.currentChessBoard % 2;
    if(decideTurn === 0) {
      return "weiß";
    }
    else {
      return "schwarz";
    }
  }

  //Analysiere den Zug wie
  checkMove(move: string) {
    //Figur wird geschlagen
    if(move.includes("x")) {
      this.figureBeaten(move);
    }
    //Rochade
    else if(move.includes("O")) {

    }
    //Normaler Zug
    else {
      this.standardMove(move);
    }
  }


  splitFigureBeaten(move: string) {
    move = move.replace("x", " ");
    return move.split(" ");
  }


  figureBeaten(move: string) {
    let turn = this.getTurn();
    if(move[0] === "K") {

    }
    else if(move[0] === "Q") {

    }
    else if(move[0] === "R") {

    }
    else if(move[0] === "B") {

    }
    else if(move[0] === "N") {

    }
    else {
      let parameters = this.splitFigureBeaten(move);
      if(turn === "wei0") {

      }
      else {

      }
    }
  }


  //Ein normaler Zug
  standardMove(move: string) {
    let turn = this.getTurn();
    console.log(turn);
    if(move[0] === "K") {

    }
    else if(move[0] === "Q") {

    }
    else if(move[0] === "R") {

    }
    else if(move[0] === "B") {

    }
    else if(move[0] === "N") {

    }
    else {
      let positions = this.getPositions(move);
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "p");
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "P");
      }
    }
  }


  updateChessBoard(position1: number, position2: number, figure: string) {
    //Verändere eine Kopie des aktuellen chessboards
    let prevChessBoard = this.copyChessBoard(this.currentChessBoard);
    prevChessBoard[position1][position2] = figure;
    //Entferne die entsprechende Figur
    this.removeFigure(prevChessBoard, position1, position2, figure);
    //Füge das fertige chessboard allen chessboards hinzu und  gehe zum nächsten chessboard
    this.chessBoards.push(prevChessBoard);
    this.currentChessBoard++;
    console.log(this.chessBoards)
  }

  //Entfernt eine Figur in der Matrix
  removeFigure(chessBoard: string[][], position1: number, position2: number, figure: string) {
    //Entferne einen weißen Bauer
    if(figure === "p") {
      if(chessBoard[position1 - 1][position2] === "p") {
        chessBoard[position1 - 1][position2] = " ";
      }
      else if(chessBoard[position1 - 2][position2] === "p") {
        chessBoard[position1 - 2][position2] = " ";
      }
    }
    //Entferne einen schwarzen Bauer
    else if(figure === "P") {
      if(chessBoard[position1 + 1][position2] === "P") {
        chessBoard[position1 + 1][position2] = " ";
      }
      else if(chessBoard[position1 + 2][position2] === "P") {
        chessBoard[position1 + 2][position2] = " ";
      }
    }
  }

  removeBeatenFigure() {

  }

  evaluateMoves() {
    console.log(this.allMoves[0]);
    console.log(this.allMoves[1]);
    this.checkMove(this.allMoves[0]);
    this.checkMove(this.allMoves[1]);
    this.checkMove(this.allMoves[2]);
  }

}


