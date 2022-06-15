import { Component, OnInit } from '@angular/core';
import { Message } from '../model/message';
import { User } from '../model/user';
import { MessageService } from '../service/message.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  message = new Message(null, null, null, "", "");
  users: any[] = [];

  constructor(private messageService : MessageService, public userService : UserService) { }

  ngOnInit(): void { 
    this.userService.getRegisteredUsers();
  }

  public onClickSubmit() {
    this.message.sender = this.userService.user;
    this.messageService.sendMessage(this.message).subscribe();
  }
}
