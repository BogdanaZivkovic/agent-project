import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {

  model = new User('', '');

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  onSubmit() {
    console.log(this.model);
    this.userService.signIn(this.model);
  }
}
