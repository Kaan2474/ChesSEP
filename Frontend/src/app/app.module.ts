import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';

import { LoginComponent } from './PreLogin/login/login.component';

import { HomepageComponent } from './PostLogin/homepage/homepage.component';
import { RegistrationComponent } from './PreLogin/registration/registration.component';


@NgModule({
  declarations: [
    AppComponent,

    LoginComponent,

    HomepageComponent,
      RegistrationComponent,

  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
