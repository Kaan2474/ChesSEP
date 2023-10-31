import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './PreLogin/login/login.component';
import { HomepageComponent } from './PostLogin/homepage/homepage.component';
import { HeaderComponent } from "./PostLogin/header/header.component";
import { AddfriendComponent } from "./PostLogin/addfriend/addfriend.component";




@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomepageComponent,
  ],
  imports: [
    BrowserModule,
    HeaderComponent,
    AddfriendComponent

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
