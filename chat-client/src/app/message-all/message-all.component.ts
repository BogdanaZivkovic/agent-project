import { Component, OnInit } from '@angular/core';
import { Message } from '../model/message';
import { User } from '../model/user';
import { MessageService } from '../service/message.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-message-all',
  templateUrl: './message-all.component.html',
  styleUrls: ['./message-all.component.css']
})
export class MessageAllComponent implements OnInit {

  message = new Message(null, null, null, "", "");
  constructor(private messageService : MessageService, public userService : UserService) { }

  ngOnInit(): void {
  }

  public onClickSubmit() {
    this.message.sender = this.userService.user;
    this.messageService.sendMessageToAll(this.message).subscribe();
  }
  
}


