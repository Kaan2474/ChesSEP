import { Component } from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute, Router} from "@angular/router";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  user:User;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService:UserService)
              {this.user=new User()}


  onSubmit()
  {
    this.userService.login(this.user).subscribe(result => {this.goToAuthentication()}, (error) => {this.errorWithForm();})
  }

  private errorWithForm()
  {
    alert("Ungültige Eingabe! Überprüfe deine Angabe!");
  }

  goToAuthentication(){
    localStorage.setItem("ActiveUser", this.user.email);
    this.router.navigate(["/Authentication"]);
  }

}
