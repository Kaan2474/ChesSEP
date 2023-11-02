import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';

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
    CreatePlayAgainstUserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterOutlet,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
