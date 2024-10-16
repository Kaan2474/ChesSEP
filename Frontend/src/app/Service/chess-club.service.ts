import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ChessClub} from "../Modules/ChessClub";
import {User} from "../Modules/User";


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
    return this.http.get<ChessClub[]>(this.URL + "/getAllChessClubs", {headers: this.header});

  }

  getMyChessClubname(){
    return this.http.get(this.URL + "/getMeinChessClubName", {headers: this.header});
  }

  getMembers(id: any){
    return this.http.get<User[]>(`${this.URL}/getMember/${id}`, {headers: this.header});

  }
  getChessClubOf(id: any){
    return this.http.get<String>(`${this.URL}/getChessClubOf/${id}`, {headers: this.header});
  }
  getChessClubById(id:any){
    return this.http.get<ChessClub>(`${this.URL}/findChessClubById/${id}`,{headers: this.header});
  }
  leaveChessClub(chessclubId:any){
    return this.http.get(`${this.URL}/leaveClub/${chessclubId}`, {headers: this.header});
  }
}
