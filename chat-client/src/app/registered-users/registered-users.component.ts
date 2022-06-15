import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { User } from '../model/user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-registered-users',
  templateUrl: './registered-users.component.html',
  styleUrls: ['./registered-users.component.css']
})
export class RegisteredUsersComponent implements OnInit {

  constructor(public userService : UserService) { }

  ngOnInit(): void {
    this.userService.getRegisteredUsers();
  }

  sortData(sort: Sort) {
    const data = this.userService.registeredUsers.slice();
    if (!sort.active || sort.direction === '') {
      this.userService.registeredUsers = data;
      return;
    }

    this.userService.registeredUsers = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'username': return compare(a.username, b.username, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

