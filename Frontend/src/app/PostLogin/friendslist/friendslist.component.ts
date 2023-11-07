import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";



@Component({
  selector: 'app-friendslist',
  templateUrl: './friendslist.component.html',
  styleUrls: ['./friendslist.component.css']
})
export class FriendslistComponent implements OnInit{

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getFriendsList();
  }


  onGetFriendsList() {
    this.getFriendsList();
  }

   private getFriendsList() {
    this.http.get("http://localhost:8080/friend/getMyFriendlist")
      .subscribe((response) => {
        console.log(response)
      })
   }
}
