import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MessageAllComponent } from './message-all/message-all.component';
import { MessageComponent } from './message/message.component';
import { MessagesComponent } from './messages/messages.component';
import { RegisteredUsersComponent } from './registered-users/registered-users.component';
import { RegistrationComponent } from './registration/registration.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { SignedInUsersComponent } from './signed-in-users/signed-in-users.component';

const routes: Routes = [
  { path: 'register', component: RegistrationComponent},
  { path: 'sign-in', component: SignInComponent},
  { path: 'registered-users', component: RegisteredUsersComponent},
  { path: 'signed-in-users', component: SignedInUsersComponent},
  { path: 'message', component: MessageComponent},
  { path: 'message-all', component: MessageAllComponent},
  { path: 'messages', component: MessagesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
