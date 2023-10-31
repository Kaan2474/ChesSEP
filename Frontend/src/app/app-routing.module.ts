import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./PreLogin/login/login.component";
import {HomepageComponent} from "./PostLogin/homepage/homepage.component";
import {RegisterComponent} from "./PreLogin/register/register.component";
import {AddfriendComponent} from "./PostLogin/addfriend/addfriend.component";

const routes: Routes = [
  {path: "login", title:"ChesSSEP", component: LoginComponent},
  {path: "", title:"ChesSEP", component: HomepageComponent},
  {path: "register", title:"ChesSEP", component: RegisterComponent},
  {path: "addfriend", title: "ChesSEP", component: AddfriendComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
