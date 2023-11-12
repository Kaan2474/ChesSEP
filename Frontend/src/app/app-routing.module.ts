import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./PreLogin/login/login.component";
import {HomepageComponent} from "./PostLogin/homepage/homepage.component";
import {RegisterComponent} from "./PreLogin/register/register.component";
import {AddfriendComponent} from "./PostLogin/addfriend/addfriend.component";
import {UserProfilViewComponent} from "./PostLogin/user-profil-view/user-profil-view.component";
import {FriendProfilViewComponent} from "./PostLogin/friend-profil-view/friend-profil-view.component";
import {CreatePlayAgainstUserComponent} from "./PostLogin/create-play-against-user/create-play-against-user.component";
import {AuthenticateComponent} from "./PreLogin/authenticate/authenticate.component";
import {InvitationComponent} from "./PostLogin/invitation/invitation.component";
import {CreatePlayAgainstComputerComponent} from "./PostLogin/create-play-against-computer/create-play-against-computer.component";
import {FriendslistComponent} from "./PostLogin/friendslist/friendslist.component";
import {JoingameComponent} from "./PostLogin/joingame/joingame.component";
import {NgForm} from '@angular/forms';
import {HidegameComponent} from "./PostLogin/hidegame/hidegame.component";



const routes: Routes = [
  {path: "", title:"ChesSSEP", component: LoginComponent},
  {path: "homepage", title:"ChesSEP", component: HomepageComponent},
  {path: "register", title:"ChesSEP", component: RegisterComponent},
  {path: "addfriend", title: "ChesSEP", component: AddfriendComponent},
  {path: "user-profil-view", title: "ChesSEP", component: UserProfilViewComponent},
  {path: "friend-profil-view/:userId", title: "ChesSEP", component:FriendProfilViewComponent},
  {path: "create-play-against-user", title: "ChesSEP", component:CreatePlayAgainstUserComponent},
  {path: "authenticate", title: "ChesSEP", component:AuthenticateComponent},
  {path: "invitation", title: "ChesSEP", component:InvitationComponent},
  {path: "create-play-against-computer", title: "ChesSEP", component:CreatePlayAgainstComputerComponent},
  {path: "friendslist", title: "ChesSEP", component: FriendslistComponent},
  {path: "joingame", title: "ChesSEP", component: JoingameComponent},
  {path: "hidegame", title: "ChesSEP", component: HidegameComponent},
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
