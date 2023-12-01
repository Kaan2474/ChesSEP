import {Component, OnInit} from '@angular/core';
import {UserService} from "../../Service/user.service";

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit{

  leaderboardlist: any[] = [];

 constructor(
   private userService: UserService
 ) {
 }

 ngOnInit(){
 this.userService.getLeaderboard().subscribe(data=>
 this.leaderboardlist = data)
 }


}
