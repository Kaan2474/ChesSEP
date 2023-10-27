import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { PreLoginComponent } from './pre-login/pre-login.component';
import { LoginComponent } from './PreLogin/login/login.component';
import { StartseiteComponent } from './PostLogin/startseite/startseite.component';
import { HomepageComponent } from './PostLogin/homepage/homepage.component';
import { RegistrierungComponent } from './PreLogin/registrierung/registrierung.component';

@NgModule({
  declarations: [
    AppComponent,
    PreLoginComponent,
    LoginComponent,
    StartseiteComponent,
    HomepageComponent,
    RegistrierungComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
