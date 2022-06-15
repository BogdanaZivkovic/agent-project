import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { User } from '../model/user';
import { MessageService } from '../service/message.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  constructor(public messageService : MessageService, public userService : UserService) { }

  ngOnInit(): void {
    this.messageService.getAllMessages(this.userService.user.username).subscribe();
  }

  sortData(sort: Sort) {
    const data = this.messageService.messages.slice();
    if (!sort.active || sort.direction === '') {
      this.messageService.messages = data;
      return;
    }

    this.messageService.messages = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'sender': return compare((a.sender as User).username, (b.sender as User).username, isAsc);
        case 'subject': return compare(a.subject, b.subject, isAsc);
        case 'content': return compare(a.content, b.content, isAsc);
        case 'date': return compare(a.date as unknown as string, b.date as unknown as string, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

