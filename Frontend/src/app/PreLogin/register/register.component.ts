import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../Service/user.service";
import {User} from "../../Modules/User";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  user: User;
  passwordRepeat:any;
  constructor(
    private router: Router,
    private userService: UserService,
  ) {
    this.user = new User(
    )
  }

  onSubmit() {
    if(this.user.passwort != this.passwordRepeat){
      this.errorWithForm();
      return;
    }
      this.userService.register(this.user).subscribe(result => {
        this.goToLogin()
      }, (error) => {
        this.errorWithForm();
      })
  }


  goToLogin() {
    this.router.navigate(["/"]);
  }

  private errorWithForm() {
    alert("Ungültige Eingabe! Überprüfe deine Angabe!");
  }


}





