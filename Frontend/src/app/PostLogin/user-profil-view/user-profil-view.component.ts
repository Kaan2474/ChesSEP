import {Component, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";




@Component({
  selector: 'app-user-profil-view',
  templateUrl: './user-profil-view.component.html',
  styleUrls: ['./user-profil-view.component.css']
})
export class UserProfilViewComponent implements OnInit {

  user: any;
  token = localStorage.getItem("JWT");
  selectedFile: File | null = null;
  url = "assets/images/profil-picture-icon.png"


  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {
    this.user = new User()


  }


  ngOnInit() {
    this.getUserDetail();

  }

  getUserDetail() {
    this.userService.getProfil(this.user).subscribe((data) => {
        console.log(data)
        this.user = data

        if(this.user.profilbild!=null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }

      },
      error => {
        console.error("Fehler beim Laden der Benutzerdaten");
      });
  }

  onSelect(event: any) {
    // Das ausgewählte File-Objekt wird dem selectedFile zugewiesen
    this.selectedFile = event.target.files[0];
    this.imageUpload()
  }

  imageUpload() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append("user-profile-view", this.selectedFile);

      

      this.userService.uploadpicture(formData,this.user).subscribe((response) => {
            console.log('Bild erfolgreich hochgeladen', response);
            this.getUserDetail();
          },
          error => {
            console.error('Fehler beim Hochladen des Bildes', error);
          }
        );
    }
  }
}












