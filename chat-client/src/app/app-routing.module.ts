import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MessageAllComponent } from './message-all/message-all.component';
import { MessageComponent } from './message/message.component';
import { MessagesComponent } from './messages/messages.component';
import { RegisteredUsersComponent } from './registered-users/registered-users.component';
import { RegistrationComponent } from './registration/registration.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { SignedInUsersComponent } from './signed-in-users/signed-in-users.component';
import { RunningAgentsComponent } from './running-agents/running-agents.component';
import { AgentTypesComponent } from './agent-types/agent-types.component';
import { PerformativesComponent } from './performatives/performatives.component';
import { ClothingComponent } from './clothing/clothing.component';

const routes: Routes = [
  { path: 'register', component: RegistrationComponent},
  { path: 'sign-in', component: SignInComponent},
  { path: 'registered-users', component: RegisteredUsersComponent},
  { path: 'signed-in-users', component: SignedInUsersComponent},
  { path: 'message', component: MessageComponent},
  { path: 'message-all', component: MessageAllComponent},
  { path: 'messages', component: MessagesComponent},
  { path: 'running-agents', component: RunningAgentsComponent},
  { path: 'agent-types', component: AgentTypesComponent},
  { path: 'performatives', component: PerformativesComponent},
  { path: 'clothing', component: ClothingComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
