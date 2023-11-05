import { Component } from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-authenticate',
  templateUrl: './authenticate.component.html',
  styleUrls: ['./authenticate.component.css']
})
export class AuthenticateComponent{
  user:User;
  code:String="";

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService) {this.user = new User()}

  submitCode(){
    this.user.email = localStorage.getItem("ActiveUser");
    this.user.twoFactor = this.code;
    this.userService.checkCode(this.user).subscribe((res) =>
      {this.goToHomePage()},
      (error) => {
        this.errorWithForm()
      })
  }
  goToHomePage(){
    this.router.navigate(["/homepage"]);
  }
  errorWithForm(){
    alert("Ungültige Eingabe! Überprüfe deine Angabe!");
  }
}
