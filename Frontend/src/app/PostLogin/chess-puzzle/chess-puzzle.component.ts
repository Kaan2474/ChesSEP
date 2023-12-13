import { Component } from '@angular/core';
import { MatchmakingService } from 'src/app/Service/matchmaking.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-chess-puzzle',
  templateUrl: './chess-puzzle.component.html',
  styleUrls: ['./chess-puzzle.component.css']
})
export class ChessPuzzleComponent {
  Allinfo:any;
  fileContent:any;

  constructor(private matchmakingService:MatchmakingService,private router:Router){
    
  }
  onFileSelected(event: any): void {
    const file: File = event.target.files[0];

    if (file) {
      this.readFileContent(file);
    }
  }

  readFileContent(file: File): void {
    const reader: FileReader = new FileReader();

    reader.onload = (e: any) => {
      const content: string = e.target.result;
      this.fileContent=content;
      this.matchmakingService.getPuzzleInfo(content).subscribe(data=>{
        this.Allinfo=data;
        console.log(this.Allinfo);
      })
    };

    reader.readAsText(file);
  }

  buttonManager(id: any){
    this.matchmakingService.startPuzzle(this.fileContent,id+1).subscribe(data=>{
      this.router.navigate(["/play-game-against-user"]);
    })
  }


}
