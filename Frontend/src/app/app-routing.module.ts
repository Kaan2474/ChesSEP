import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./PreLogin/login/login.component";
import {HomepageComponent} from "./PostLogin/homepage/homepage.component";

const routes: Routes = [
  {path: "login", title:"Login", component: LoginComponent},
  {path: "", title:"HomePage", component: HomepageComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
