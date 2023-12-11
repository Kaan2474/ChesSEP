import {Component, OnInit} from '@angular/core';
import {FriendsService} from "../../../Service/friends.service";
import {Friends} from "../../../Modules/Friends";
import {User} from "../../../Modules/User";
import {UserService} from "../../../Service/user.service";
import {ActivatedRoute,Router} from "@angular/router";
import {Chat} from "../../../Modules/Chat";
import {ChatService} from "../../../Service/chat.service";

@Component({
  selector: 'app-addmembers',
  templateUrl: './add-members.component.html',
  styleUrls: ['./add-members.component.css']
})
export class AddMembersComponent implements OnInit{
  public friends: Friends[] = [];
  user: User;
  id:any;
  memberIds:any = [];
  groupName:any;


  constructor(private friendsService: FriendsService, private userService: UserService, private route: ActivatedRoute, private chatService: ChatService,private router: Router) {
    this.user = new User();
    this.refreshUser();
  }
  ngOnInit() {
    this.id = this.route.snapshot.params["id"];
    console.log('userId:', this.id);
    this.getFriends();
    this.refreshUser();
  }
  refreshUser() {
    this.userService.getUserbyToken()
      .subscribe(data => {
        this.user = data;
      });

  }
  getFriends() {
    this.friendsService.getFriendslist()
      .subscribe((data) => {
        console.log(data)
        this.friends = data;
      });
  }
  addMembers(friendID: any) {
    const element = document.getElementById(friendID) as HTMLElement;
    if (this.memberIds.includes(friendID)!==true) {
      this.memberIds.push(friendID);
      element.style.display = "none";
    }
    console.log(this.memberIds);
  }

  createGroup() {
    const groupname = (document.getElementById("gruppenname") as HTMLInputElement).value;
    this.groupName = groupname;
    if (groupname === "") {
      alert("Sie müssen einen Namen für die Gruppe eintragen!");
    } else if (this.memberIds.length < 2) {
      alert("Sie können keine Gruppe mit weniger als 3 Mitgliedern erstellen.");
    } else {
      this.chatService.createGroupChat(groupname, this.memberIds).subscribe(()=> {
        alert(`Gruppenchat: ${groupname} wurde erfolgreich erstellt`);
        this.router.navigate(['/groupchat/' + this.groupName]);
        localStorage.setItem("GroupName", this.groupName);
        }
      );
    }
  }


}
