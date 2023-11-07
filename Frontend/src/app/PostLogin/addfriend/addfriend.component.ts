import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";


@Component({
  selector: 'app-addfriend',
  templateUrl: './addfriend.component.html',
  styleUrls: ['./addfriend.component.css']
})
export class AddfriendComponent {

  constructor(private http: HttpClient) { }

  //Für Freund hinzufügen Backend Kommunikation
  onAddFriend(user: {addUser:string}) {
    console.log(user)
    this.http.post("http://localhost:8080/friend/sendFriendRequest", user)
      .subscribe((response) => {
        console.log(response);
    });
  }

}
