import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./PreLogin/login/login.component";
import {HomepageComponent} from "./PostLogin/homepage/homepage.component";
import {RegisterComponent} from "./PreLogin/register/register.component";

const routes: Routes = [
  {path: "login", title:"Login", component: LoginComponent},
  {path: "", title:"HomePage", component: HomepageComponent},
  {path: "register", title:"Register", component: RegisterComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
