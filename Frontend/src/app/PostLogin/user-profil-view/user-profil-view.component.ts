import {Component, OnInit} from '@angular/core';
import {User} from "../../Modules/User";
import {UserService} from "../../Service/user.service";
import {ChessClubService} from "../../Service/chess-club.service";
import {Chess} from "../../Modules/Chess";
import {forkJoin, Observable} from "rxjs";
import {MatchmakingService} from "../../Service/matchmaking.service";

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
  schachClubname: any;
  lastThreeGames: Chess[];
  pgnString: any;



  constructor(
    private userService: UserService,
    private chessclubservice: ChessClubService,
    private matchmakingService :MatchmakingService
  ) {
    this.user = new User()
    this.lastThreeGames = [];

  }

  ngOnInit() {
    this.getUserDetail();
    this.getMyClubName()
    this.getLastThreeGames();
  }


  matchReplay(pgnId:any){
    this.matchmakingService.pgn(pgnId).subscribe(data => {
      this.pgnString = data;
      localStorage.setItem("pgn", this.pgnString)
      location.href ="/replay-moves"

    })
  }

  getLastThreeGames() {
    this.userService.getPlayHistory().subscribe(data => {
      this.lastThreeGames = data;

    });
  }

  getElo() {
    this.lastThreeGames.forEach(data => {
      forkJoin([
        this.userService.getUser(data.playerWhiteID),
        this.userService.getUser(data.playerBlackID)
      ]).subscribe(([playerWhite, playerBlack]) => {

        if (playerWhite) {
          data.playerWhiteID = playerWhite.elo;
        }

        if (playerBlack) {
          data.playerBlackID = playerBlack.elo;
        }
      });
    });
  }


  getUserDetail() {
    this.userService.getUserbyToken().subscribe((data) => {
        console.log(data)
        this.user = data

        if(this.user.profilbild!=null){
          this.user.profilbild='data:image/png;base64,'+this.user.profilbild;
        }
        if(this.user.compleatedPuzzles >= 3){
          this.user.achievement = "Schachexperte";
        }else {
          this.user.achievement = "kein Achievement"
        }
      },);
  }

  onSelect(event: any) {
    this.selectedFile = event.target.files[0];

    this.imageUpload()
  }

  getMyClubName(){
    this.chessclubservice.getMyChessClubname().subscribe(data=>{
    this.schachClubname = data
    console.log(this.schachClubname)})
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
  changePrivacy() {
    this.userService.putStreamPrivacy().subscribe(() => {
        this.refreshUser();
      })
  }
  refreshUser() {
    this.userService.getUserbyToken()
      .subscribe(data => {
        this.user = data;
        window.location.reload();
      });

  }

  exportPgn(pgnId:any){
    this.matchmakingService.pgn(pgnId).subscribe(data => {
        this.pgnString = data;
        const link = document.createElement('a');
        link.download = 'ChesSepPgn.txt';
        const pgn = new Blob([this.pgnString], {type:'text/plain;charset=utf-8'});
        link.href = window.URL.createObjectURL(pgn);
        link.click();

        console.log(this.pgnString)
      }
    )
  }

}












