import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../Modules/User";
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
  public acceptRequest(jwtToken: any, friend: any){
    return this.http.post(this.URL + "/acceptFriendRequest",friend,{headers: this.header})
  }
  public denyRequest(jwtToken: any, friend: any){
    return this.http.post(this.URL + "/denyFriendRequest" ,friend, {headers: this.header})
  }
  public getFriendRequest(jwtToken:any){
    return this.http.get<Friends[]>(this.URL + "/getMyPendingFriendRequests",{headers: this.header});
  }

  deleteFriend(friendID: any) {
    return this.http.post(this.URL + "/deleteFriend", friendID, {headers: this.header});
  }

}
