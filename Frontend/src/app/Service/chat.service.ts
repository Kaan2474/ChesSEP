import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Chat} from "../Modules/Chat";
import {User} from "../Modules/User";
import {Message} from "../Modules/Message";

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

  public createPrivateChat(chat: Chat) {
    return this.http.post(this.userURL + "/createPrivateChat", chat,{headers: this.header});
  }
  public createGroupChat(chat: Chat) {
    return this.http.post(this.userURL + "/createGroupChat", chat, {headers: this.header});
  }
  public writeMessage(chatid:bigint, chat:Chat) {
    return this.http.post(`${this.userURL}/writeMessagePrivateChat/${chatid}`, chat,{headers: this.header});
  }
  public writeMessageGroup(chatid:bigint, chat:Chat) {
    return this.http.post(`${this.userURL}/writeMessageGroup/${chatid}`, chat,{headers: this.header});
  }
  public membersOfGroupChat(chatid:bigint) {
    return this.http.get<User[]>(`${this.userURL}/members/${chatid}`,{headers: this.header});
  }
  public findLatestMessage(chatId:bigint,time: bigint) {
        return this.http.get<Message[]>(`${this.userURL}/latest/${chatId}/${time}`,{headers: this.header});
  }

  getChangeableMessages(chatId: bigint) {
    return this.http.get<Message[]>(`${this.userURL}/changableMessage/${chatId}`, {headers:this.header});
  }

  changeMessage(chatId: bigint, chat: Chat) {
    return this.http.post(`${this.userURL}/changeMessage/${chatId}`, chat, {headers:this.header});
  }

  deleteMessage(chatId: bigint, chat: Chat) {
    return this.http.post(`${this.userURL}/deleteMessage/${chatId}`, chat, {headers:this.header});
  }

  findAllMyChats() {
    return this.http.get<Chat[]>( `${this.userURL}/allMyChats`, {headers:this.header});
  }

  getChatMessages(chatId: bigint) {
    return this.http.get<Message[]>(`${this.userURL}/getMessages/${chatId}`, {headers:this.header});
  }
}
