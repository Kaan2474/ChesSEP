import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Friends} from "../Modules/Friends";


@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  URL = "http://localhost:8080/friend"
  token = localStorage.getItem("JWT")
  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")

  constructor(private http: HttpClient) { }

  getFriendslist() {
    return this.http.get<Friends[]>(this.URL + "/getMyFriendlist", {headers: this.header});
  }


}
