import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  url="assets/images/profil-picture-icon.png"

  onSelect(event: any) {

      let fileType = event.target.files[0].type;
      if (fileType.match(/image\/*/)) {
        let reader = new FileReader();
        reader.readAsDataURL(event.target.files[0]);
        reader.onload = (event: any) => {
          this.url = event.target.result;
        };
      } else {
        window.alert('Bitte wählen Sie das richtige Bildformat');
      }
    }
  }







