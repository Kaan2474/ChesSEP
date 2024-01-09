import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatchmakingService} from "../../Service/matchmaking.service";
import {Chess} from "../../Modules/Chess";
import {Subscription, interval, of} from 'rxjs';
@Component({
  selector: 'app-streaming-ansicht',
  templateUrl: './streaming-ansicht.component.html',
  styleUrls: ['./streaming-ansicht.component.css']
})
export class StreamingAnsichtComponent implements OnInit,OnDestroy{
  id:any;
  gameID:any;
  user:User;
  timer: number[] = [];

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private matchmakinService: MatchmakingService,
    private router: Router) {
    this.user = new User();
  }
  ngOnDestroy(): void {
  }

  ngOnInit(): void {
    this.userService.getUserbyToken().subscribe(data=> this.user = data);
    this.gameID = localStorage.getItem("GameID");
    console.log(this.gameID);
  }


}
