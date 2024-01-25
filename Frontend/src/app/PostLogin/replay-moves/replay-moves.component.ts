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


  //Erstellt das erste Schachbrett
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
  }


  //Erstellt eine Kopie eines Schachbretts  --> z.B position = 3 das 4. Schachbrett aus allen Schachbrettern
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

  //Erstelle eine Kopie eines zweidimensionalen Arrays
  copyTwoDimensionalArray(chessBoard: string[][]) {
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

    for(let i = 0; i<chessBoard.length; i++) {
      for(let j = 0; j<chessBoard[i].length; j++) {
        if(chessBoard[i][j] === "r") {
          copy[i][j] = "r";
        }
        else if(chessBoard[i][j] === "n") {
          copy[i][j] = "n";
        }
        else if(chessBoard[i][j] === "b") {
          copy[i][j] = "b";
        }
        else if(chessBoard[i][j] === "q") {
          copy[i][j] = "q";
        }
        else if(chessBoard[i][j] === "k") {
          copy[i][j] = "k";
        }
        else if(chessBoard[i][j] === "p") {
          copy[i][j] = "p";
        }
        else if(chessBoard[i][j] === "R") {
          copy[i][j] = "R";
        }
        else if(chessBoard[i][j] === "N") {
          copy[i][j] = "N";
        }
        else if(chessBoard[i][j] === "B") {
          copy[i][j] = "B";
        }
        else if(chessBoard[i][j] === "Q") {
          copy[i][j] = "Q";
        }
        else if(chessBoard[i][j] === "K") {
          copy[i][j] = "K";
        }
        else if(chessBoard[i][j] === "P") {
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
  placeFigures(currentChessBoard: number) {
    let field;
    for(let i = 0; i<this.chessBoards[currentChessBoard].length; i++) {
      for(let j = 0; j<this.chessBoards[currentChessBoard][i].length; j++) {
        //Weiße Figuren
        if(this.chessBoards[currentChessBoard][i][j] === "p") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/bauer_w.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "r") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/turm_w.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "n") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/springer_w.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "b") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/laeufer_w.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "q") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/dame_w.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "k") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/koenig_w.png)";
        }

        //Schwarze Figuren
        else if(this.chessBoards[currentChessBoard][i][j] === "P") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/bauer_s.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "R") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/turm_s.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "N") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/springer_s.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "B") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/laeufer_s.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "Q") {
          field = document.getElementById(this.getNotation(i,j));
          field!.style.backgroundImage = "url(assets/figures/dame_s.png)";
        }
        else if(this.chessBoards[currentChessBoard][i][j] === "K") {
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


  //Funktion für Button: Nächster Zug
  nextChessMove() {
    if(this.currentChessMove < this.allMoves.length) {
      this.currentChessMove++;
      console.log("Aktueller Zug: " + this.currentChessMove);
      this.placeFigures(this.currentChessMove);
    }
  }

  //Funktion für Button: Vorheriger Zug
  prevChessMove() {
    this.currentChessMove--;
    console.log("Aktueller Zug: " + this.currentChessMove);
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

  //Analysiere den Zug
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


  //Spaltet den Zug auf
  splitFigureBeaten(move: string) {
    // z.B exd4 --> e d4 oder Q d4
    move = move.replace("x", " ");
    // z.B e d4 --> [e, d4]
    return move.split(" ");
  }


  //Wenn eine Figur geschlagen wird
  figureBeaten(move: string) {
    let turn = this.getTurn();
    if(move[0] === "K") {

    }

    else if(move[0] === "Q") {
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters, "q");
      }
      else {
        this.updateFigureBeaten(parameters, "Q");
      }

    }
    else if(move[0] === "R") {

    }
    else if(move[0] === "B") {

    }
    else if(move[0] === "N") {

    }
    else {
      //z.B exd4 --> [e,d4]
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters, "p");
      }
      else {
        this.updateFigureBeaten(parameters, "P");
      }
    }
  }


  //Ändert eine geschlagene Figur
  updateFigureBeaten(parameters: string[], figure: string) {
    let prevChessBoard = this.copyChessBoard(this.currentChessBoard);
    //z.B parameters = [e,d4] --> Figur auf d4 wird geschlagen --> d4 wird auf entsprechende Figur geändert
    let positions = this.getPositions(parameters[1]);
    prevChessBoard[positions[0]][positions[1]] = figure;
    //Entferne die entsprechende Figur in dem aktuellen Schachfeld
    this.removeFigureBeaten(prevChessBoard , parameters, figure);
    //Füge das fertige Schachfeld allen Schachfeldern hinzu und gehe zum nächsten Schachfeld
    this.chessBoards.push(prevChessBoard);
    this.currentChessBoard++;
    console.log(this.chessBoards)
  }

  //Vertikale Entfernung einer Figur
  verticalRemoval(halfNotation: string, chessBoard: string[][], figure: string) {
    for(let i = 1; i<9;i++) {
      halfNotation += i;
      let positions = this.getPositions(halfNotation);
      if(chessBoard[positions[0]][positions[1]] === figure) {
        chessBoard[positions[0]][positions[1]] = " ";
        break;
      }
      else {
        halfNotation = halfNotation[0];
      }
    }
  }

  //Entfernt eine geschlagene Figur
  removeFigureBeaten(chessBoard: string[][], parameters: string[], figure: string) {
    let halfNotation = parameters[0];
    //Alle Figuren ausschließlich Bauer werden entfernt
    if(halfNotation.toUpperCase() === halfNotation) {
      halfNotation = parameters[1];
      halfNotation = halfNotation[0];
      //Dame entfernen
      if(figure === "Q" || figure === "q") {
        this.verticalRemoval(halfNotation, chessBoard, figure);
      }
    }
    //Bauer wird entfernt
    else {
      halfNotation = parameters[0];
      this.verticalRemoval(halfNotation, chessBoard, figure);
    }
  }


  //Entfernt eine Figur bei einem normalen Zug
  removeFigure(chessBoard: string[][], parameter: string, figure: string) {
    //Bauer werden vertikal entfernt
    if(figure === "p" || figure === "P") {
      this.verticalRemoval(parameter, chessBoard, figure);
    }
  }



  //Ein normaler Zug: z.B Ke6 oder a5
  standardMove(move: string) {
    let turn = this.getTurn();
    //König wird bewegt
    if(move[0] === "K") {
      //z.B Ke6 --> e6
      move = move.replace("K", "");
      //Positionen für den gemachten Zug
      let positions = this.getPositions(move);
      //z.B e6 --> e
      move = move[0];
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "k", move);
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "K", move);
      }
    }
    //Dame wird bewegt
    else if(move[0] === "Q") {
      move = move.replace("Q", "");
      let positions = this.getPositions(move);
      move = move[0];
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "q", move);
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "Q", move);
      }
    }
    //Turm wird bewegt
    else if(move[0] === "R") {
      move = move.replace("R", "");
      let positions = this.getPositions(move);
      move = move[0];
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "r", move);
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "R", move);
      }
    }
    //Läufer wird bewegt
    else if(move[0] === "B") {
      move = move.replace("B", "");
      let positions = this.getPositions(move);
      move = move[0];
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "B", move);
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "b", move);
      }
    }
    //Springer wird bewegt
    else if(move[0] === "N") {
      move = move.replace("N", "");
      let positions = this.getPositions(move);
      move = move[0];
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "n", move);
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "N", move);
      }
    }
    else {
      let positions = this.getPositions(move);
      move = move[0];
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "p", move);
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "P", move);
      }
    }
  }


  updateChessBoard(position1: number, position2: number, figure: string, parameter: string) {
    //Entferne die entsprechende Figur in dem aktuellen Schachfeld
    let chessBoard1 = this.copyChessBoard(this.currentChessBoard);
    this.removeFigure(chessBoard1, parameter, figure);
    //Füge die entfernte Schachfigur an eine andere Position ein
    let chessBoard2 = this.copyTwoDimensionalArray(chessBoard1);
    chessBoard2[position1][position2] = figure;
    //Füge das fertige Schachfeld allen Schachfeldern hinzu
    this.chessBoards.push(chessBoard2);
    //Gehe zum nächsten Schachfeld
    this.currentChessBoard++;
    console.log(this.chessBoards)
  }

/*
  //Entfernt eine Figur in einem übergebenen Schachfeld
  removeFigure(chessBoard: string[][], position1: number, position2: number, figure: string) {
    //Entferne einen weißen Bauer
    if(chessBoard[position1 - 1][position2] === figure) {
      chessBoard[position1 - 1][position2] = " ";
    }
    else if(chessBoard[position1 - 2][position2] === figure) {
      chessBoard[position1 - 2][position2] = " ";
    }
    //Entferne einen schwarzen Bauer
    else if(chessBoard[position1 + 1][position2] === figure) {
      chessBoard[position1 + 1][position2] = " ";
    }
    else if(chessBoard[position1 + 2][position2] === figure) {
        chessBoard[position1 + 2][position2] = " ";
    }

  }
*/

  evaluateMoves() {
    for(let i = 0; i<6; i++) {
      this.checkMove(this.allMoves[i]);
    }
  }

}


