import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../Service/friends.service";
import {Friends} from "../../Modules/Friends";
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {ChatService} from "../../Service/chat.service";
import {Chat} from "../../Modules/Chat";


@Component({
  selector: 'app-friendslist',
  templateUrl: './friendslist.component.html',
  styleUrls: ['./friendslist.component.css']
})
export class FriendslistComponent implements OnInit{
  public allFriends: Friends[] = [];
  user: User;
  privacyText: any;
  chat: any;
  id:any;


  constructor(private friendsService: FriendsService, private userService: UserService, private route: ActivatedRoute, private chatService: ChatService,private router: Router) {
    this.user = new User();
    this.chat= new Chat();
    this.refreshUser();
  }
  ngOnInit() {
    this.id = this.route.snapshot.params["id"];
    console.log('userId:', this.id);
    this.getFriends();
    this.refreshUser();
  }

  /*Gibt den aktuellen User anhand des Tokens zurück und speichert diesen in der Variable user*/
  refreshUser() {
    this.userService.getUserbyToken()
      .subscribe(data => {
        this.user = data;
        this.setPrivacy();
      });

  }


  /*Zeigt die Freundesliste an*/
  getFriends() {
    this.friendsService.getFriendslist()
      .subscribe((data) => {
        console.log(data)
        this.allFriends = data;
      });
  }

  /*Setzt den aktuellen Text der Privatsphäre von der Freundesliste*/
  setPrivacy() {
    this.privacyText = "Status der Privatsphäre: "+this.user.privacy;
  }

  /*Ändert die Privatsphäre des Users*/
  changePrivacy() {
    this.userService.putPrivacy()
      .subscribe(data => {
        this.refreshUser();
      })
    }

    /*Löscht den Freund, wenn man auf ,,Löschen'' drückt, anhand der ID des Freundes*/
    onDeleteFriend(friend: {id: number}) {
    this.friendsService.deleteFriend(friend)
      .subscribe(data => {
        console.log(1);
      })
      window.location.reload();
    }

  createPrivateChat(friendId: number){

      this.chatService.createPrivateChat(friendId).subscribe();
      console.log("userID:" + friendId)
      this.router.navigate(['/privateChat/' + friendId])
  }
}
