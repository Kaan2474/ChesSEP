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
  url = "assets/images/profil-picture-icon.png"
  selectedFile: File | null = null;
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

    const formData = new FormData();
      formData.append("vorname", this.user.vorname);
      formData.append("nachname", this.user.nachname);
      formData.append("email", this.user.email);
      formData.append("passwort", this.user.passwort);
      formData.append("geburtsdatum", this.user.geburtsdatum);

    if(this.selectedFile!=null)
      formData.append("bild",this.selectedFile);

    this.userService.register(formData).subscribe(result => {
      this.imageUpload()
        this.goToLogin()
      }, (error) => {
        this.errorWithForm();
    })
    
  }

  onSelect(event: any) {
    this.selectedFile = event.target.files[0];
    this.showSelectedImage()
  }

  imageUpload() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append("user-profile-view", this.selectedFile);

      this.userService.uploadpicture(formData,this.user).subscribe((response) => {


          console.log('Bild erfolgreich hochgeladen', response);

        },
        error => {
          console.error('Fehler beim Hochladen des Bildes', error);
        }
      );
    }
  }

  showSelectedImage() {
    if (this.selectedFile) {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.url = e.target.result;
      };

      reader.readAsDataURL(this.selectedFile);
    }
  }

  goToLogin() {
    this.router.navigate(["/"]);
  }

  private errorWithForm() {
    alert("Ungültige Eingabe! Überprüfe deine Angabe!");
  }




}





