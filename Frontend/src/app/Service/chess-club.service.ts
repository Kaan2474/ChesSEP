import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ChessClub} from "../Modules/ChessClub";


@Injectable({
  providedIn: 'root'
})
export class ChessClubService {
  URL = "http://localhost:8080/ChessClub"
  token = localStorage.getItem("JWT")
  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")

  constructor(private http: HttpClient) { }

  joinClub(){
    return this.http.post(this.URL + "/joinClub", {headers: this.header});

  }

  joinClubV2(){
    return this.http.post(this.URL + "/joinClubV2", {headers: this.header});

  }

  createClub(){
    return this.http.post(this.URL + "/createClub", {headers: this.header});

  }

  createClubV2(name: ChessClub){
    return this.http.post(this.URL + "/createClubV2", name, {headers: this.header});

  }

  getAllChessClubs(){
    return this.http.get(this.URL + "/getAllChessClubs", {headers: this.header});

  }

  getMyChessClubname(){
    return this.http.get(this.URL + "/getMeinChessClubName", {headers: this.header});

  }

  getAllMembers(){
    return this.http.get(this.URL + "/getMembers", {headers: this.header});

  }
}
