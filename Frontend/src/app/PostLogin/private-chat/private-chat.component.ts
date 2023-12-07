import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatService} from "../../Service/chat.service";
import {Message} from "../../Modules/Message";

@Component({
  selector: 'app-private-chat',
  templateUrl: './private-chat.component.html',
  styleUrls: ['./private-chat.component.css']
})
export class PrivateChatComponent implements OnInit,OnDestroy{
  token = localStorage.getItem("JWT");
  id: any;
  messages:Message[]=[];
  newMessage:Message;
  content:any;
  user:any;
  friend:any;

  constructor(private userService: UserService,
              private chatService : ChatService,
              private route: ActivatedRoute,
              private router: Router) {

              this.user = new User();
              this.friend = new User();
              this.newMessage = new Message();
              }

  ngOnInit() {
    this.getUserDetail();
    this.getFriendDetails();
  }
  ngOnDestroy(): void {
  }
  getUserDetail() {
    this.userService.getUserbyToken().subscribe((data) => {
      console.log(data)
      this.user = data;

    },);
  }
  getFriendDetails(){
    this.id = this.route.snapshot.params["id"];
    this.userService.getUser(this.id).subscribe((data) => {
      console.log(data)
      this.friend = data
    },);
  }



}
