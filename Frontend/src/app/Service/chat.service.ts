import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Chat} from "../Modules/Chat";
import {User} from "../Modules/User";

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

  public createPrivateChat(friendid:number) {
    return this.http.get(`${this.userURL}/createPrivateChat/${friendid}`,{headers: this.header});
  }
  public createGroupChat(name:String, users:any[]) {
    return this.http.get(`${this.userURL}/createGroupChat/${name}/${users}`, {headers: this.header});
  }
  public writeMessage(chatid:bigint, chat:Chat) {
    return this.http.post(`${this.userURL}/writeMessagePrivateChat/${chatid}`, chat,{headers: this.header});
  }
  public writeMessageGroup(chatid:bigint, chat:Chat) {
    return this.http.post(`${this.userURL}/writeMessageGroup/${chatid}`, chat,{headers: this.header});
  }
  public membersOfGroupChat(chatid:bigint) {
    return this.http.get<BigInt[]>(`${this.userURL}/members/${chatid}`,{headers: this.header});
  }
  public findLatestMessage(chatId:bigint,time: bigint) {
        return this.http.get<Chat[]>(`${this.userURL}/latest/${chatId}/${time}`,{headers: this.header});
  }

  getChangeableMessages(chatId: bigint) {
    return this.http.get<Chat[]>(`${this.userURL}/changableMessage/${chatId}`, {headers:this.header});
  }

  changeMessage(chatId: bigint, chat: Chat) {
    return this.http.post(`${this.userURL}/changeMessage/${chatId}`, chat, {headers:this.header});
  }

  deleteMessage(chatId: bigint, chat: Chat) {
    return this.http.post(`${this.userURL}/deleteMessage/${chatId}`, chat, {headers:this.header});
  }

  findAllMyGroupChats() {
    return this.http.get<Chat[]>( `${this.userURL}/allMyGroupChats`, {headers:this.header});
  }

  getChatMessages(chatId: bigint,lastMessageTime:bigint) {
    return this.http.get<Chat[]>(`${this.userURL}/getMessages/${chatId}/${lastMessageTime}`, {headers:this.header});
  }
  getMyPrivateChatWith(friendId:bigint){
    return this.http.get<Chat>(`${this.userURL}/getMyPrivateChatWith/${friendId}`, {headers:this.header})
  }
  getGroupByGroupName(groupName:any){
    return this.http.get<Chat>(`${this.userURL}/getGroupByGroupName/${groupName}`, {headers:this.header})
  }

  getChesClubChatByName(chessClubName: any){
    return this.http.get<Chat>(`${this.userURL}/getChessClubChatByName/${chessClubName}`, {headers: this.header})

  }

  leaveGroupChat(privateGroupName:String){
      return this.http.get(`${this.userURL}/leaveGroup/${privateGroupName}`, {headers:this.header});
  }
}
