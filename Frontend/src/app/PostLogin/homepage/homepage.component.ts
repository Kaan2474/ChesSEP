import { Component } from '@angular/core';
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {Friends} from "../../Modules/Friends";
import {ChessClubService} from "../../Service/chess-club.service";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ChessClub} from "../../Modules/ChessClub";



@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {

  public allgroups: Friends[] = [];

  allChessClubs: ChessClub[] = [];


  URL = "http://localhost:8080/ChessClub"
  token = localStorage.getItem("JWT")
  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")

  constructor(private matchmakingservice:MatchmakingService,
              private chessclubservice: ChessClubService,
              private http: HttpClient,
              private router: Router){

  }

  ngOnInit(){
    var waited=localStorage.getItem("Waited");
    if(waited=="1"){
      this.matchmakingservice.cancelMatchRequest().subscribe();
      this.matchmakingservice.dequeueMatch().subscribe();

      localStorage.setItem("Waited","0");
    }
    this.getAllChessClubs()
  }

  createClub(name: {name: string}){
    console.log(name.name)
    this.http.get(`${this.URL}/createClubV2/${name.name}`, {headers: this.header}).subscribe(data =>

      {
        this.showNotification("Der Schachclub wurde erstellt");
      },
      error => {
        console.log("Schachclub konnte nicht erstellt werden", error)
        alert('Fehler beim Erstellen des Schachclubs');
      }
    );
    window.location.reload();
  }


  joinClub(name: {name: string}) {
    console.log(name.name)
    this.http.get(`${this.URL}/joinClubV2/${name.name}`  ,{ headers: this.header }).subscribe(
      (data) => {
          this.showNotification(`Du bist dem Schachclub "${name.name}" beigetreten.`);

      },
      (error) => {
        console.log("Fehler beim Beitritt zum Schachclub", error);
        alert(`Fehler beim Beitritt zum Schachclub "${name.name}"`);
      }
    );
  }

  getAllChessClubs(){
    this.chessclubservice.getAllChessClubs().subscribe(data=>
    this.allChessClubs = data)

  }






  showNotification(message:string){
    alert(message);
  }

}
