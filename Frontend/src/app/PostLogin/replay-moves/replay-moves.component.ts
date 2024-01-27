import { Component } from '@angular/core';
import {compareSegments} from "@angular/compiler-cli/src/ngtsc/sourcemaps/src/segment_marker";

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


  //Entfernt die Leerzeichen am Anfang und Ende aus allen Elementen von moves
  removeBlanks() {
    for(let i = 0; i<this.allMoves.length; i++) {
      this.allMoves[i] = this.allMoves[i].trim();
    }
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
    if(this.currentChessMove < this.allMoves.length - 1) {
      this.currentChessMove++;
      this.placeFigures(this.currentChessMove);
    }
    else if(this.currentChessMove === this.allMoves.length - 1) {
      this.checkLastMove();
    }
  }

  //Funktion für Button: Vorheriger Zug
  prevChessMove() {
    if(this.currentChessMove === 0) {
      alert("Es gibt keine vorherigen Züge!");
    }
    else {
      this.currentChessMove--;
      this.placeFigures(this.currentChessMove);
    }
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


  //Evaluiere jeden Zug
  evaluateMoves() {
    for(let i = 0; i<this.allMoves.length - 1; i++) {
      this.checkMove(this.allMoves[i]);
    }
    console.log(this.chessBoards);
  }



  //Analysiere den Zug
  checkMove(move: string) {
    //Entferne das + --> irrelevant
    if(move.includes("+")) {
      move = move.replace("+", "");
    }
    //Figur wird geschlagen
    if(move.includes("x")) {
      this.figureBeaten(move);
    }
    //Rochade
    else if(move.includes("O")) {
      this.castling(move);
    }
    //Normaler Zug
    else {
      this.standardMove(move);
    }
  }


  //Prüfe den letzten Zug
  checkLastMove() {
    let lastMove = this.allMoves[this.allMoves.length - 1];
    if(lastMove === "1-0") {
      alert("Der weiße Spieler hat gewonnen!");
    }
    else if(lastMove === "0-1") {
      alert("Der schwarze Spieler hat gewonnen!");
    }
    else if(lastMove === "1/2-1/2") {
      alert("Unentschieden!");
    }
    else if(lastMove === "*") {
      alert("Partie ist noch nicht beendet!!");
    }
  }

  //Spiel vorbei: Entweder weiß oder schwarz hat gewonnen
  gameFinished(move: string) {
    let turn = this.getTurn();
    if(turn === "weiß") {

    }
    else {

    }
  }


  //Kurze und Lange Rochade
  castling(move: string) {
    let turn = this.getTurn();
    let chessBoard: string[][] = this.copyChessBoard(this.currentChessBoard);
    if(turn === "weiß") {
      //Lange Rochade
      if(move === "O-O-O") {
        chessBoard[0][2] = "k";
        chessBoard[0][3] = "r";
        chessBoard[0][0] = " ";
        chessBoard[0][4] = " ";
      }
      //Kurze Rochade
      else {
        chessBoard[0][5] = "r";
        chessBoard[0][6] = "k";
        chessBoard[0][4] = " ";
        chessBoard[0][7] = " ";
      }
    }

    else {
      //Lange Rochade
      if(move === "O-O-O") {
        chessBoard[7][2] = "K";
        chessBoard[7][3] = "R";
        chessBoard[7][0] = " ";
        chessBoard[7][4] = " ";
      }
      //Kurze Rochade
      else {
        chessBoard[7][5] = "R";
        chessBoard[7][6] = "K";
        chessBoard[7][4] = " ";
        chessBoard[7][7] = " ";
      }
    }
    //Füge das überarbeitete Schachbrett hinzu
    this.chessBoards.push(chessBoard);
    //Gehe zum nächsten Schachbrett
    this.currentChessBoard++;
  }


  //Spaltet den Zug auf, wenn eine Figur geschlagen wurde
  splitFigureBeaten(move: string) {
    //Ersetze das x durch ein Leerzeichen: z.B exd4 --> e d4
    move = move.replace("x", " ");
    // z.B e d4 --> [e, d4]
    return move.split(" ");
  }


  //Wenn eine Figur geschlagen wird
  figureBeaten(move: string) {
    let turn = this.getTurn();
    //König schlägt Figur
    if(move[0] === "K") {
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters[1], "k");
      }
      else {
        this.updateFigureBeaten(parameters[1], "K");
      }
    }
    //Dame schlägt Figur
    else if(move[0] === "Q") {
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters[1], "q");
      }
      else {
        this.updateFigureBeaten(parameters[1], "Q");
      }
    }
    //Turm schlägt Figur
    else if(move[0] === "R") {
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters[1], "r");
      }
      else {
        this.updateFigureBeaten(parameters[1], "R");
      }
    }
    //Läufer schlägt Figur
    else if(move[0] === "B") {
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters[1], "b");
      }
      else {
        this.updateFigureBeaten(parameters[1], "B");
      }
    }
    //Springer schlägt Figur
    else if(move[0] === "N") {
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters[1], "n");
      }
      else {
        this.updateFigureBeaten(parameters[1], "N");
      }
    }
    //Bauer schlägt Figur
    else {
      //z.B parameters = exd4 --> [e,d4]
      let parameters = this.splitFigureBeaten(move);
      if(turn === "weiß") {
        this.updateFigureBeaten(parameters[1], "p");
      }
      else {
        this.updateFigureBeaten(parameters[1], "P");
      }
    }
  }


  //Ändert eine geschlagene Figur
  updateFigureBeaten(parameter: string, figure: string) {
    /*Entferne die entsprechende Figur in dem aktuellen Schachfeld
      z.B parameter = d4 --> Figur auf d4 wird geschlagen
     */
    let chessBoard1 = this.copyChessBoard(this.currentChessBoard);
    this.removeFigureBeaten(chessBoard1, parameter, figure);
    let chessBoard2 = this.copyTwoDimensionalArray(chessBoard1);
    let positions = this.getPositions(parameter);
    //Füge die entfernte Schachfigur an eine andere Position ein
    chessBoard2[positions[0]][positions[1]] = figure;
    //Füge das fertige Schachfeld allen Schachfeldern hinzu und gehe zum nächsten Schachfeld
    this.chessBoards.push(chessBoard2);
    //Gehe zum nächsten Schachfeld
    this.currentChessBoard++;
  }



  //Entfernt eine geschlagene Figur
  removeFigureBeaten(chessBoard: string[][], parameter: string, figure: string) {
    let positions = this.getPositions(parameter);
    //Weißer Bauer wird entfernt
    if(figure === "p") {
      if(chessBoard[positions[0]-1][positions[1]-1] === figure) {
        chessBoard[positions[0]-1][positions[1]-1] = " ";
      }
      else if(chessBoard[positions[0]-1][positions[1]+1] === figure) {
        chessBoard[positions[0]-1][positions[1]+1] = " ";
      }
    }
    //Schwarzer Bauer wird entfernt
    else if(figure === "P") {
      if(chessBoard[positions[0]+1][positions[1]+1] === figure) {
        chessBoard[positions[0]+1][positions[1]+1] = " ";
      }
      else if(chessBoard[positions[0]+1][positions[1]-1] === figure) {
        chessBoard[positions[0]+1][positions[1]-1] = " ";
      }
    }
    //Weiße oder schwarze Dame wird entfernt
    else if(figure === "q" || figure === "Q") {
      this.removeQueen(chessBoard, positions[0], positions[1], figure)
    }
    //Weißer oder schwarzer Turm wird entfernt
    else if(figure === "r" || figure === "R") {
      this.removeRook(chessBoard, positions[0], positions[1], figure)
    }
    //Weißer oder schwarzer Läufer wird entfernt
    else if(figure === "b" || figure === "B") {
      this.removeBishop(chessBoard, positions[0], positions[1], figure)
    }
    //Weißer oder schwarzer Springer wird entfernt
    else if(figure === "n" || figure === "N") {
      this.removeKnight(chessBoard, positions[0], positions[1], figure)
    }
    //Weißer oder schwarzer König wird entfernt
    else if(figure === "k" || figure === "K") {
      console.log("Muss noch implementiert werden")
    }
  }



  //Entfernt eine Figur bei einem normalen Zug
  removeFigure(chessBoard: string[][], position1: number, position2: number, figure: string) {
    //Weißer Bauer wird entfernt
    if(figure === "p") {
      if(chessBoard[position1-1][position2] === "p") {
        chessBoard[position1-1][position2] = " ";
      }
      else if(chessBoard[position1-2][position2] === "p") {
        chessBoard[position1-2][position2] = " ";
      }
    }
    //Schwarzer Bauer wird entfernt
    else if(figure === "P") {
      if(chessBoard[position1+1][position2] === "P") {
        chessBoard[position1+1][position2] = " ";
      }
      else if(chessBoard[position1+2][position2] === "P") {
        chessBoard[position1+2][position2] = " ";
      }
    }
    //Weißer oder schwarzer Springer wird entfernt
    else if(figure === "n" || figure === "N") {
      this.removeKnight(chessBoard, position1, position2, figure);
    }
    //Weiße oder schwarze Dame wird entfernt
    else if(figure === "q" || figure === "Q") {
      this.removeQueen(chessBoard, position1, position2, figure);
    }
    //Weißer oder schwarzer Läufer wird entfernt
    else if(figure === "b" || figure === "B") {
      this.removeBishop(chessBoard, position1, position2, figure);
    }
    //Weißer Turm oder schwarzer Turm wird entfernt
    else if(figure === "r" || figure === "R") {
      this.removeRook(chessBoard, position1, position2, figure)
    }
    //Weißer oder schwarzer König wird entfernt
    else if(figure === "k" || figure === "K") {
      this.removeKing(chessBoard, position1, position2, figure);
    }
  }

  //Entferne einen weißen oder schwarzen König
  removeKing(chessBoard: string[][], position1: number, position2: number, figure: string) {
    this.removeDiagonal(chessBoard, position1, position2, figure);
    this.removeHorizontal(chessBoard, position1, position2, figure);
    this.removeVertical(chessBoard, position1, position2, figure);
  }


  //Entferne einen weißen oder schwarzen Turm
  removeRook(chessBoard: string[][], position1: number, position2: number, figure: string) {
    this.removeHorizontal(chessBoard, position1, position2, figure);
    this.removeVertical(chessBoard, position1, position2, figure);
  }

  //Entferne einen weißen oder schwarzen Läufer
  removeBishop(chessBoard: string[][], position1: number, position2: number, figure: string) {
    this.removeDiagonal(chessBoard, position1, position2, figure);
  }

  //Entferne eine weiße oder schwarze Dame
  removeQueen(chessBoard: string[][], position1: number, position2: number, figure: string) {
    this.removeDiagonal(chessBoard, position1, position2, figure);
    this.removeHorizontal(chessBoard, position1, position2, figure);
    this.removeVertical(chessBoard, position1, position2, figure);
  }

  //Entferne einen weißen oder schwarzen Springer
  removeKnight(chessBoard: string[][], position1: number, position2: number, figure: string) {
    if(chessBoard[position1+2][position2-1] === figure) {
      chessBoard[position1+2][position2-1] = " ";
    }
    else if(chessBoard[position1+2][position2+1] === figure) {
      chessBoard[position1+2][position2+1] = " ";
    }
    else if(chessBoard[position1-2][position2-1] === figure) {
      chessBoard[position1-2][position2-1] = " ";
    }
    else if(chessBoard[position1-2][position2+1] === figure) {
      chessBoard[position1-2][position2+1] = " ";
    }
    else if(chessBoard[position1+1][position2+2] === figure) {
      chessBoard[position1+1][position2+2] = " ";
    }
    else if(chessBoard[position1-1][position2+2] === figure) {
      chessBoard[position1-1][position2+2] = " ";
    }
    else if(chessBoard[position1+1][position2-2] === figure) {
      chessBoard[position1+1][position2-2] = " ";
    }
    else if(chessBoard[position1-1][position2-2] === figure) {
      chessBoard[position1-1][position2-2] = " ";
    }
  }



  //Entfernt eine bestimmte Figur horizontal
  removeHorizontal(chessBoard: string[][], position1: number, position2: number, figure: string) {
    if(position2 === 0) {
      //Wenn man ganz links ist
      for(let i = position2; i<8; i++) {
        if(chessBoard[position1][i] === figure) {
          chessBoard[position1][i] = " ";
          return;
        }
        //Damit der richtige Turm entfernt wird
        else if(chessBoard[position1][i] != figure && chessBoard[position1][i] != " ") {
          break;
        }
      }
    }
    //Wenn man ganz rechts ist
    else if(position2 === 7) {
      for(let i = position2; i>=0; i--) {
        if(chessBoard[position1][i] === figure) {
          chessBoard[position1][i] = " ";
          return;
        }
        //Damit der richtige Turm entfernt wird
        else if(chessBoard[position1][i] != figure && chessBoard[position1][i] != " ") {
          break;
        }
      }
    }
    //Wenn man in der Mitte ist
    else {
      //Suche links
      for(let i = position2; i>=0; i--) {
        if(chessBoard[position1][i] === figure) {
          chessBoard[position1][i] = " ";
          return;
        }
        //Damit der richtige Turm entfernt wird
        else if(chessBoard[position1][i] != figure && chessBoard[position1][i] != " ") {
          break;
        }
      }
      //Suche rechts
      for(let i = position2; i<8; i++) {
        if(chessBoard[position1][i] === figure) {
          chessBoard[position1][i] = " ";
          return;
        }
        //Damit der richtige Turm entfernt wird
        else if(chessBoard[position1][i] != figure && chessBoard[position1][i] != " ") {
          break;
        }
      }
    }
  }

  //Entfernt eine bestimmte Figur vertikal
  removeVertical(chessBoard: string[][], position1: number, position2: number, figure: string) {
    if(position1 === 0) {
      //Wenn man ganz unten ist
      for(let i = position1; i<8; i++) {
        if(chessBoard[i][position2] === figure) {
          chessBoard[i][position2] = " ";
          return;
        }
      }
    }
    //Wenn man ganz oben ist
    else if(position1 === 7) {
      for(let i = position1; i>=0; i--) {
        if(chessBoard[i][position2] === figure) {
          chessBoard[i][position2] = " ";
          return;
        }
      }
    }
    //Wenn man in der Mitte ist
    else {
      //Suche unten
      for(let i = position1; i>=0; i--) {
        if(chessBoard[i][position2] === figure) {
          chessBoard[i][position2] = " ";
          return;
        }
      }
      //Suche oben
      for(let i = position1; i<8; i++) {
        if(chessBoard[i][position2] === figure) {
          chessBoard[i][position2] = " ";
          return;
        }
      }
    }
  }

  //Entfernt eine bestimmte Figur diagonal
  removeDiagonal(chessBoard: string[][], position1: number, position2: number, figure: string) {
    //links oben
    for(let i = position1 + 1, j = position2 - 1; i <= 7 && j >= 0; i++, j--) {
      if(chessBoard[i][j] === figure) {
        chessBoard[i][j] = " ";
        return;
      }
    }
    //rechts oben
    for(let i = position1 + 1, j = position2 + 1; i <= 7 && j <= 7; i++, j++) {
      if(chessBoard[i][j] === figure) {
        chessBoard[i][j] = " ";
        return;
      }
    }
    //rechts unten
    for(let i = position1 - 1, j = position2 + 1; i >= 0 && j <= 7; i--, j++) {
      if(chessBoard[i][j] === figure) {
        chessBoard[i][j] = " ";
        return;
      }
    }
    //links unten
    for(let i = position1 - 1, j = position2 - 1; i >= 0 && j >= 0; i--, j--) {
      if(chessBoard[i][j] === figure) {
        chessBoard[i][j] = " ";
        return;
      }
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
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "k");
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "K");
      }
    }
    //Dame wird bewegt
    else if(move[0] === "Q") {
      move = move.replace("Q", "");
      let positions = this.getPositions(move);
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "q");
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "Q");
      }
    }
    //Turm wird bewegt
    else if(move[0] === "R") {
      move = move.replace("R", "");
      if(move.length === 3) {
        this.specialUpdate(move, turn, "R");
      }
      else {
        let positions = this.getPositions(move);
        if(turn === "weiß") {
          this.updateChessBoard(positions[0], positions[1], "r");
        }
        else {
          this.updateChessBoard(positions[0], positions[1], "R");
        }
      }
    }
    //Läufer wird bewegt
    else if(move[0] === "B") {
      move = move.replace("B", "");
      let positions = this.getPositions(move);
      if(turn === "weiß") {
        this.updateChessBoard(positions[0], positions[1], "b");
      }
      else {
        this.updateChessBoard(positions[0], positions[1], "B");
      }
    }
    //Springer wird bewegt
    else if(move[0] === "N") {
      move = move.replace("N", "");
      //z.B move = ge2
      if(move.length === 3) {
        this.specialUpdate(move, turn, "N");
      }
      //z.B move = g2
      else {
        let positions = this.getPositions(move);
        if(turn === "weiß") {
          this.updateChessBoard(positions[0], positions[1], "n");
        }
        else {
          this.updateChessBoard(positions[0], positions[1], "N");
        }
      }
    }
    //Bauer wird bewegt
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


  //Normaler Zug: Figur entfernen und an andere Position einfügen
  updateChessBoard(position1: number, position2: number, figure: string) {
    //Entferne die entsprechende Figur in dem aktuellen Schachfeld
    let chessBoard1 = this.copyChessBoard(this.currentChessBoard);
    this.removeFigure(chessBoard1, position1, position2, figure);
    //Füge die entfernte Schachfigur an eine andere Position ein
    let chessBoard2 = this.copyTwoDimensionalArray(chessBoard1);
    chessBoard2[position1][position2] = figure;
    //Füge das fertige Schachfeld allen Schachfeldern hinzu
    this.chessBoards.push(chessBoard2);
    //Gehe zum nächsten Schachfeld
    this.currentChessBoard++;
  }


  /*Spezielle Änderung des Schachfeldes: Wenn zwei gleiche Figuren auf das selbe Feld platziert werden kann
  --> z.B Nge2
   */
  specialUpdate(move: string, turn: string, figure: string) {
    if(turn === "weiß") {
      figure = figure.toLowerCase();
    }
    //Die Position zum Hinzufügen der Figur
    let addNotation = move[1] + move[2];
    let addPositions = this.getPositions(addNotation);
    //Die Position zum Löschen der Figur
    let removeNotation = move[0] + 1;
    let removePositions = this.getPositions(removeNotation);
    //Entferne die Figur anhand der Spalte
    let chessBoard1 = this.copyChessBoard(this.currentChessBoard);
    this.removeVertical(chessBoard1, removePositions[0], removePositions[1], figure);
    //Füge die entfernte Schachfigur an eine andere Position ein
    let chessBoard2 = this.copyTwoDimensionalArray(chessBoard1);
    chessBoard2[addPositions[0]][addPositions[1]] = figure;
    //Füge das fertige Schachfeld allen Schachfeldern hinzu
    this.chessBoards.push(chessBoard2);
    //Gehe zum nächsten Schachfeld
    this.currentChessBoard++;
  }


}


