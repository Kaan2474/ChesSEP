import { Component } from '@angular/core';
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {Friends} from "../../Modules/Friends";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {

  public allgroups: Friends[] = [];
  constructor(private matchmakingservice:MatchmakingService){

  }

  ngOnInit(){
    var waited=localStorage.getItem("Waited");
    if(waited=="1"){
      this.matchmakingservice.cancelMatchRequest().subscribe();
      this.matchmakingservice.dequeueMatch().subscribe();

      localStorage.setItem("Waited","0");
    }
  }
}
