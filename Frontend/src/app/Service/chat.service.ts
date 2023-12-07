import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../Modules/User";
import {Message} from "../Modules/Message";
import {GroupMessage} from "../Modules/GroupMessage";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})

export class ChatService {

  private userURL: string;
  token = localStorage.getItem("JWT")

  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")

  constructor(private http: HttpClient,
  ) {
    this.userURL = "http://localhost:8080/chat";

  }

  public createPrivateChat(friend: User) {
    return this.http.post(this.userURL + "/createPrivateChat", friend,{headers: this.header});
  }
  public createGroupChat(user: User) {
    return this.http.post(this.userURL + "/createGroupChat", user, {headers: this.header});
  }
  public leaveGroup(user: User) {
    return this.http.post(this.userURL + "/leaveGroup", user, {headers: this.header});
  }
  public deleteGroupChat(name: String) {
    return this.http.post(this.userURL + "/deleteGroupChat/{privateGroupName}", name,{headers: this.header});
  }
  public deletePrivatChat(friend: User) {
    return this.http.post(this.userURL + "/deletePrivateChat", friend,{headers: this.header});
  }
  public addMember(user: User) {
    return this.http.put(this.userURL + "/{chatId}/addMember", user,{headers: this.header});
  }
  public writeMessage(user: User) {
    return this.http.post(this.userURL + "/writeMessagePrivateChat/{chatId}", user,{headers: this.header});
  }
  public writeMessageGroup(user: User) {
    return this.http.post(this.userURL + "/writeMessageGroup/{chatId}", user,{headers: this.header});
  }
  public membersOfGroupChat(groupName: string) {
    return this.http.post<User[]>(this.userURL + "/members/{chatId}/{groupName}", groupName,{headers: this.header});
  }
  public findLatestMessage(time: bigint) {
        return this.http.post<Message[]>(this.userURL + "/members/{chatId}/{groupName}", time,{headers: this.header});
  }
}
