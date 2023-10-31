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


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomepageComponent,
    RegisterComponent,
    PreHeaderComponent,
    HeaderComponent,
    AddfriendComponent
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
