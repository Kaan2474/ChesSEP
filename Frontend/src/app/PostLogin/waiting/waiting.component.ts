import {Component, OnDestroy, OnInit} from '@angular/core';
import { Router} from "@angular/router";
import {Chess} from "../../Modules/Chess";
import {interval, Subscription} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-waiting',
  templateUrl: './waiting.component.html',
  styleUrls: ['./waiting.component.css']
})
export class WaitingComponent implements OnInit, OnDestroy{

  URL = "http://localhost:8080/match";
  sub: Subscription = new Subscription();
  token = localStorage.getItem("JWT");
  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
  chessGame: any;
  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit() {
    localStorage.setItem("Waited", "1");
    this.waitForMatch(this.chessGame);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
  goToHomepage() {
    this.router.navigate(["/homepage"]);
  }

  waitForMatch(chess: Chess) {
    this.sub = interval(250).subscribe(data => {
      this.http.get(this.URL + "/getMyCurrentMatch", {headers: this.header}).subscribe(chess => {
        if(chess!=null){
          this.chessGame = chess;
          console.log(this.chessGame.timeStamp)
        }
        if (this.chessGame != null) {
          console.log(this.chessGame.gameID)
          this.router.navigate(["/play-game-against-user"]);
        }
      });
    });
  }
}
