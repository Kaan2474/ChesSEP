import {Component, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ActivatedRoute} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import {ChessClubService} from "../../Service/chess-club.service";




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
    private chessclubservice: ChessClubService
  ) {
    this.user = new User()

  }

  ngOnInit() {
    this.getUserDetail();
    this.getMyClubName()
  }

  getUserDetail() {
    this.userService.getUserbyToken().subscribe((data) => {
        console.log(data)
        this.user = data

        if(this.user.profilbild!=null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
      },);
  }

  onSelect(event: any) {
    this.selectedFile = event.target.files[0];

    this.imageUpload()
  }

  getMyClubName(){
    this.chessclubservice.getMyChessClubname().subscribe(data=>{
    this.user = data
    console.log(data)})

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












