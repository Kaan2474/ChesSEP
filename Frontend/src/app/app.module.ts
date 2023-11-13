import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './PreLogin/login/login.component';
import { HomepageComponent } from './PostLogin/homepage/homepage.component';
import {RouterOutlet} from "@angular/router";
import { RegisterComponent } from './PreLogin/register/register.component';
import { PreHeaderComponent } from './PreLogin/pre-header/pre-header.component';
import { HeaderComponent } from "./PostLogin/header/header.component";
import { AddfriendComponent } from "./PostLogin/addfriend/addfriend.component";
import { UserProfilViewComponent } from './PostLogin/user-profil-view/user-profil-view.component';
import { FriendProfilViewComponent } from './PostLogin/friend-profil-view/friend-profil-view.component';
import { CreatePlayAgainstUserComponent } from './PostLogin/create-play-against-user/create-play-against-user.component';
import { AuthenticateComponent } from './PreLogin/authenticate/authenticate.component';
import { InvitationComponent } from './PostLogin/invitation/invitation.component';
import { CreatePlayAgainstComputerComponent } from './PostLogin/create-play-against-computer/create-play-against-computer.component';
import {FormsModule} from "@angular/forms";
import {FriendslistComponent} from "./PostLogin/friendslist/friendslist.component";
import {JoingameComponent} from "./PostLogin/joingame/joingame.component";
import { HidegameComponent } from './PostLogin/hidegame/hidegame.component';
import { PlayGameAgainstComputerComponent } from './PostLogin/play-game-against-computer/play-game-against-computer.component';
import { PlayGameAgainstUserComponent } from './PostLogin/play-game-against-user/play-game-against-user.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomepageComponent,
    RegisterComponent,
    PreHeaderComponent,
    HeaderComponent,
    AddfriendComponent,
    UserProfilViewComponent,
    FriendProfilViewComponent,
    CreatePlayAgainstUserComponent,
    AuthenticateComponent,
    InvitationComponent,
    CreatePlayAgainstComputerComponent,
    FriendslistComponent,
    JoingameComponent,
    HidegameComponent,
    PlayGameAgainstComputerComponent,
    PlayGameAgainstUserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterOutlet,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
