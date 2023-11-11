import {Component, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute} from "@angular/router";
import {HttpHeaders} from "@angular/common/http";



@Component({
  selector: 'app-user-profil-view',
  templateUrl: './user-profil-view.component.html',
  styleUrls: ['./user-profil-view.component.css']
})
export class UserProfilViewComponent implements OnInit {

  user: any;
  token = localStorage.getItem("JWT");

  url ="assets/images/profil-picture-icon.png"


  constructor(
    private userService: UserService,
    private route: ActivatedRoute
  ) {this.user=new User()

  }


  ngOnInit() {
    this.getUserDetail();
  }

  getUserDetail(){
    this.userService.getProfil(this.user).subscribe(  (data)=> {
      console.log(data)
      this.user=data


  },
  error => {
  console.error("Fehler beim Laden der Benutzerdaten")
})

}

}


