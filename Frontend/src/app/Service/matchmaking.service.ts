import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../Modules/User";
import {Friends} from "../Modules/Friends";


@Injectable({
  providedIn: 'root'
})
export class MatchmakingService {
  private URL: string;
  token = localStorage.getItem("JWT")

  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")


  constructor(private http: HttpClient) {
    this.URL = "http://localhost:8080/match";

  }

  public getMyMatchInvitations(jwtToken: any){
    return this.http.get<User[]>(this.URL + "/getMyMatchInvitations",{headers: this.header});
  }

  public acceptMatchRequest(jwtToken:any,friend: User){
    return this.http.post(this.URL + "/acceptMatchRequest",friend, {headers: this.header})
  }

  public denyMatchRequest(){
    return this.http.get(this.URL + "/denyMatchRequest", {headers: this.header})
  }

  public queueMatch(){
    return this.http.get(this.URL + "/queueMatch", {headers: this.header})
  }

  public dequeueMatch(){
    return this.http.get(this.URL + "/dequeueMatch", {headers: this.header})
  }

  public getMyCurrentMatch(){
    return this.http.get(this.URL + "/getMyCurrentMatch", {headers: this.header})
  }

  public getMyCurrentEnemy(){
    return this.http.get(this.URL + "/getMyCurrentEnemy", {headers: this.header})
  }
}
